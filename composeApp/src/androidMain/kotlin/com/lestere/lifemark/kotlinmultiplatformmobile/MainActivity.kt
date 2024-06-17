package com.lestere.lifemark.kotlinmultiplatformmobile

import App
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import cache.AndroidContents
import data.platform.Permission

@Suppress("DEPRECATION")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        this.enableEdgeToEdge()

        setContent {
            AndroidContents.localContext = this
            App()
        }
    }

    override fun onStart() {
        super.onStart()
        Permission.checkPermission(this)
    }

    override fun onResume() {
        super.onResume()
        if (Permission.isPermissionGranted(this)) {
            Log.i("PERMISSION", "Granted successfully.")
        }
    }

    @Deprecated(
        """This method has been deprecated in favor of using the Activity Result API
      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt
      contracts for common intents available in
      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for
      testing, and allow receiving results in separate, testable classes independent from your
      activity. Use
      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)} passing
      in a {@link RequestMultiplePermissions} object for the {@link ActivityResultContract} and
      handling the result in the {@link ActivityResultCallback#onActivityResult(Object) callback}."""
    )
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Permission.REQUEST_CODE) {
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted failed.")
                    return
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppAndroidPreview() {
    App()
}