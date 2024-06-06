package data.platform

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permission {
    const val REQUEST_CODE = 5

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /**
     * MARK:
     *  1. checkPermission return pare of value
     *  2. Has permission: PackageManager.PERMISSION_GRANTED
     *  3. No permission: PackageManager.PERMISSION_DENIED
     */
    @SuppressLint("ObsoleteSdkInt")
    fun isPermissionGranted(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return permissions.all { permission ->
                ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }

    fun checkPermission(activity: Activity): Boolean {
        return if (isPermissionGranted(activity)) {
            true
        } else {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE)
            false
        }
    }
}