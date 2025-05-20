package com.example.eapp_admin.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

/**
 * Tiện ích để kiểm tra và yêu cầu quyền lưu trữ
 */
class PermissionUtils(
    private val activity: ComponentActivity,
    private val permissionLauncher: ActivityResultLauncher<Array<String>>
)
{
    // Callback để xử lý sau khi yêu cầu quyền
    private var onPermissionGranted: (() -> Unit)? = null

    /** Gọi từ UI */
    fun checkAndRequestStoragePermission(onGranted: () -> Unit) {
        onPermissionGranted = onGranted

        if (hasStoragePermission()) {
            onGranted()
        } else {
            requestStoragePermission()
        }
    }

    /* -------------------- private helpers -------------------- */

    private fun hasStoragePermission(): Boolean =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                ContextCompat.checkSelfPermission(
                    activity, Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ->
                /* API 29-32: ghi Downloads không cần WRITE, chỉ cần READ → luôn true */
                true

            else ->  /* API ≤ 28 */
                ContextCompat.checkSelfPermission(
                    activity, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
        }

    private fun requestStoragePermission() {
        val permissions: Array<String> =
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO
                    )

                Build.VERSION.SDK_INT <= Build.VERSION_CODES.P ->
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                else ->                                     /* API 29-32 */
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        /* ️⃣  Sử dụng launcher đã truyền */
        permissionLauncher.launch(arrayOf(permissions.toString()))
    }

    /** gọi từ ActivityResult callback (nếu muốn xử lý tại đây) */
    fun onPermissionResult(permissions: Map<String, Boolean>) {
        val allGranted = permissions.values.all { it }
        if (allGranted) onPermissionGranted?.invoke()
        else {
            android.widget.Toast
                .makeText(activity, "Ứng dụng cần quyền lưu file PDF", android.widget.Toast.LENGTH_LONG)
                .show()
        }
    }
}



// Khởi tạo ActivityResultLauncher để xử lý kết quả từ việc yêu cầu quyền
//    private val requestPermissionLauncher = activity.registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        val allGranted = permissions.entries.all { it.value }
//        if (allGranted) {
//            onPermissionGranted?.invoke()
//        } else {
//            // Hiển thị thông báo khi người dùng từ chối cấp quyền
//            android.widget.Toast.makeText(
//                activity,
//                "Ứng dụng cần quyền lưu trữ để lưu file PDF",
//                android.widget.Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//
//    /**
//     * Kiểm tra và yêu cầu quyền cần thiết để lưu file PDF
//     */
//    fun checkAndRequestStoragePermission(onGranted: () -> Unit) {
//        this.onPermissionGranted = onGranted
//
//        if (hasStoragePermission()) {
//            onGranted()
//        } else {
//            requestStoragePermission()
//        }
//    }
//
//    /**
//     * Kiểm tra xem đã có quyền lưu trữ chưa
//     */
//    private fun hasStoragePermission(): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            // Android 13+ sử dụng quyền cụ thể hơn
//            ContextCompat.checkSelfPermission(
//                activity,
//                Manifest.permission.READ_MEDIA_IMAGES
//            ) == PackageManager.PERMISSION_GRANTED
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            // Android 10+ không cần quyền WRITE_EXTERNAL_STORAGE cho thư mục Downloads
//            true
//        } else {
//            // Android 9 và thấp hơn cần quyền WRITE_EXTERNAL_STORAGE
//            ContextCompat.checkSelfPermission(
//                activity,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) == PackageManager.PERMISSION_GRANTED
//        }
//    }
//
//    /**
//     * Yêu cầu quyền lưu trữ dựa trên phiên bản Android
//     */
//    private fun requestStoragePermission() {
//        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            // Android 13+ yêu cầu quyền cụ thể hơn
//            arrayOf(
//                Manifest.permission.READ_MEDIA_IMAGES,
//                Manifest.permission.READ_MEDIA_VIDEO
//            )
//        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//            // Android 9 và thấp hơn cần quyền WRITE_EXTERNAL_STORAGE
//            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        } else {
//            // Android 10-12 không cần quyền WRITE_EXTERNAL_STORAGE cho thư mục Downloads
//            // Nhưng cần READ_EXTERNAL_STORAGE
//            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//
//        requestPermissionLauncher.launch(permissions)
//    }