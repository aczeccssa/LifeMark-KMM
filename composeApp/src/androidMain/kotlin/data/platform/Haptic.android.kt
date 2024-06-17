package data.platform

import android.app.Service
import android.os.Vibrator
import cache.AndroidContents

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "DEPRECATION")
actual object Haptic {
    actual fun playHapticPattern(style: HapticStyle) {
        AndroidContents.localContext?.also {
            val vibrator = it.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?
            vibrator?.vibrate(longArrayOf(0, 75), -1)
        }
    }
}