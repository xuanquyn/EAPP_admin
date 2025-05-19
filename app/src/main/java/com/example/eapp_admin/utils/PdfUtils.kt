package com.example.eapp_admin.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.compositionContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/** Capture Compose UI -> PDF, sau đó lưu */
suspend fun captureToPdf(
    activity: ComponentActivity,
    //view: View,
    //Here
    composable: @Composable () -> Unit,
    //Here
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>
) {

    val permissionUtils = PermissionUtils(activity, requestPermissionLauncher)

    permissionUtils.checkAndRequestStoragePermission {
        //createAndSavePdf(activity, view)

        //Here
        /* tạo ComposeView ẩn, render lại composable */
        val cv = ComposeView(activity).apply {
            /* 1️⃣  lấy CompositionContext của cửa sổ hiện tại */
            val parent = (activity.window.decorView as View).compositionContext
            setParentCompositionContext(parent)

            /* 2️⃣  tùy chọn – dọn dẹp khi ComposeView bị bỏ */
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnDetachedFromWindow
            )

            /* 3️⃣  render nội dung cần in */
            setContent { composable() }
        }

        // đo WRAP_CONTENT -> lấy hết chiều cao LazyColumn
        val wSpec = View.MeasureSpec.makeMeasureSpec(
            activity.window.decorView.width, View.MeasureSpec.EXACTLY)
        val hSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        cv.measure(wSpec, hSpec)
        cv.layout(0, 0, cv.measuredWidth, cv.measuredHeight)

        // bitmap + PDF như cũ
        val bitmap = Bitmap.createBitmap(cv.measuredWidth, cv.measuredHeight, Bitmap.Config.ARGB_8888)
        cv.draw(Canvas(bitmap))

        saveBitmapToPdf(activity, bitmap)   // hàm bạn đã có
        //Here
    }
    Log.d("PDF_captureToPdf", "run capturetoPDF")

}

/* ------------------------------------------------------------------------- */

private fun createAndSavePdf(activity: ComponentActivity, view: View) {
    val bitmap = captureViewToBitmap(view)
    saveBitmapToPdf(activity, bitmap)
}

/** chụp *bất kỳ* View (AndroidComposeView hoặc ComposeView) thành Bitmap */
private fun captureViewToBitmap(view: View): Bitmap {
    Log.d("PDF_captureViewToBitmap", "run captureViewToBitmap")

    var w = view.width
    var h = view.height
    if (w == 0 || h == 0) {
        // view chưa đo => đo lại với WRAP_CONTENT
        val widthSpec  = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(widthSpec, heightSpec)
        w = view.measuredWidth
        h = view.measuredHeight
        view.layout(0, 0, w, h)
    }

    val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

/** lưu Bitmap thành PDF (Downloads nếu có quyền; riêng app nếu không) */
private fun saveBitmapToPdf(context: Context, bitmap: Bitmap) {
    Log.d("PDF_start_saveBitmapToPdf", "run captureViewToBitmap")

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    page.canvas.drawBitmap(bitmap, 0f, 0f, null)
    pdfDocument.finishPage(page)

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "EAPP_Revenue_$timeStamp.pdf"

    /* Chọn thư mục lưu tùy API */
    val dir: File = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    } else {
        // Scoped-Storage: lưu về bộ nhớ riêng -> luôn thành công, không cần quyền
        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!
    }

    val filePath = File(dir, fileName)

    try {
        FileOutputStream(filePath).use { pdfDocument.writeTo(it) }
        android.widget.Toast.makeText(
            context,
            "PDF đã lưu: ${filePath.absolutePath}",
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
        pdfDocument.close()
    }

    Log.d("PDF_saveBitmapToPdf", "run saveBitmapToPdf")

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