package com.example.eapp_admin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.compositionContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Capture Compose UI -> PDF, sau đó lưu
 */
suspend fun captureToPdf(
    activity: ComponentActivity,
    composable: @Composable () -> Unit,
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>
) {
    val permissionUtils = PermissionUtils(activity, requestPermissionLauncher)

    permissionUtils.checkAndRequestStoragePermission {
        // 1️⃣ Tạo ComposeView ẩn để render nội dung cần in
        val composeView = ComposeView(activity).apply {
            // Lấy compositionContext từ window decorView
            val parentContext = (activity.window.decorView as View).compositionContext
            setParentCompositionContext(parentContext)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
            setContent { composable() }
        }

        // 2️⃣ Tạo container chứa ComposeView và add lên window
        val container = FrameLayout(activity).apply {
            visibility = View.INVISIBLE
            addView(composeView)
        }
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        activity.addContentView(container, params)

        // 3️⃣ Đợi view attach & measured rồi capture
        composeView.post {
            // Đo với giới hạn chiều cao hữu hạn để tránh infinite constraints
            val maxHeight = activity.window.decorView.height
            val widthSpec = View.MeasureSpec.makeMeasureSpec(
                activity.window.decorView.width,
                View.MeasureSpec.EXACTLY
            )
            val heightSpec = View.MeasureSpec.makeMeasureSpec(
                maxHeight,
                View.MeasureSpec.AT_MOST
            )
            composeView.measure(widthSpec, heightSpec)
            composeView.layout(0, 0, composeView.measuredWidth, composeView.measuredHeight)

            // Tạo bitmap và vẽ
            val bitmap = Bitmap.createBitmap(
                composeView.measuredWidth,
                composeView.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
            composeView.draw(Canvas(bitmap))

            // 4️⃣ Lưu thành PDF
            saveBitmapToPdf(activity, bitmap)

            // 5️⃣ Remove view tạm
            (container.parent as? ViewGroup)?.removeView(container)
        }
    }
}

/**
 * Lưu Bitmap thành PDF
 */
private fun saveBitmapToPdf(context: Context, bitmap: Bitmap) {
    val pdfDoc = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
    val page = pdfDoc.startPage(pageInfo)
    page.canvas.drawBitmap(bitmap, 0f, 0f, null)
    pdfDoc.finishPage(page)

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "EAPP_Revenue_$timeStamp.pdf"

    val dir: File = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    } else {
        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!! // Scoped storage
    }
    val file = File(dir, fileName)

    try {
        FileOutputStream(file).use { pdfDoc.writeTo(it) }
        android.widget.Toast.makeText(
            context,
            "PDF đã lưu: ${file.absolutePath}",
            android.widget.Toast.LENGTH_LONG
        ).show()
    } catch (e: Exception) {
        android.widget.Toast.makeText(
            context,
            "Không thể lưu PDF: ${e.message}",
            android.widget.Toast.LENGTH_LONG
        ).show()
        e.printStackTrace()
    } finally {
        pdfDoc.close()
    }
}

//package com.example.eapp_admin.utils
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.pdf.PdfDocument
//import android.os.Environment
//import android.view.View
//import androidx.activity.ComponentActivity
//import androidx.activity.result.ActivityResultLauncher
//import java.io.File
//import java.io.FileOutputStream
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
///**
// * Capture Compose UI to PDF and save to Downloads folder
// */
//suspend fun captureToPdf(
//    activity: ComponentActivity,
//    view: View,
//    requestPermissionLauncher: ActivityResultLauncher<Array<String>>
//)
//{
//    val permissionUtils = PermissionUtils(activity, requestPermissionLauncher)
//
//    val context = activity.applicationContext
//
//    permissionUtils.checkAndRequestStoragePermission {
//        // Gọi hàm tạo PDF khi đã có quyền
//        createAndSavePdf(activity, view)
//    }
//}
//
///**
// * Tạo và lưu PDF sau khi đã có quyền
// */
//private fun createAndSavePdf(activity: ComponentActivity, view: View) {
//    val context = activity.applicationContext
//
//    // Tạo bitmap từ view
//    val bitmap = captureComposeViewToBitmap(view)
//
//    // Tạo và lưu PDF
//    saveBitmapToPdf(context, bitmap)
//}
//
///**
// * Chụp ComposeView thành bitmap
// */
//private fun captureComposeViewToBitmap(view: View): Bitmap {
//    // Lấy kích thước của view
//    val width = view.width
//    val height = view.height
//
//    // Tạo bitmap với kích thước tương ứng
//    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//
//    // Tạo canvas với bitmap
//    val canvas = Canvas(bitmap)
//
//    // Vẽ view lên canvas
//    view.draw(canvas)
//
//    return bitmap
//}
//
///**
// * Lưu bitmap thành file PDF
// */
//private fun saveBitmapToPdf(context: Context, bitmap: Bitmap) {
//    // Tạo tài liệu PDF
//    val pdfDocument = PdfDocument()
//
//    // Tạo thông tin trang với kích thước bitmap
//    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
//
//    // Bắt đầu trang mới
//    val page = pdfDocument.startPage(pageInfo)
//
//    // Vẽ bitmap lên trang
//    val canvas = page.canvas
//    canvas.drawBitmap(bitmap, 0f, 0f, null)
//
//    // Hoàn thành trang
//    pdfDocument.finishPage(page)
//
//    // Tạo timestamp cho tên file
//    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//    val fileName = "EAPP_Revenue_$timeStamp.pdf"
//
//    // Lấy thư mục Downloads
//    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//    val filePath = File(downloadsDir, fileName)
//
//    try {
//        // Lưu tài liệu PDF vào file
//        val fileOutputStream = FileOutputStream(filePath)
//        pdfDocument.writeTo(fileOutputStream)
//        fileOutputStream.close()
//
//        // Hiển thị thông báo thành công
//        android.widget.Toast.makeText(
//            context,
//            "PDF đã được lưu vào thư mục Downloads với tên $fileName",
//            android.widget.Toast.LENGTH_LONG
//        ).show()
//    } catch (e: Exception) {
//        e.printStackTrace()
//        // Hiển thị thông báo lỗi
//        android.widget.Toast.makeText(
//            context,
//            "Không thể lưu PDF: ${e.message}",
//            android.widget.Toast.LENGTH_LONG
//        ).show()
//    } finally {
//        // Đóng tài liệu PDF
//        pdfDocument.close()
//    }
//}