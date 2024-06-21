package data.platform

import android.app.Service
import android.os.Vibrator
import cache.AndroidContents
import io.github.aakira.napier.Napier

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "DEPRECATION")
actual object Haptic {
    actual fun playHapticPattern(style: HapticStyle) {
        AndroidContents.localContext?.also {
            val vibrator = it.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?
            vibrator?.vibrate(style.value.longArray, style.value.repeater)
            return
        }
        Napier.w("Failed to call vibrate, device might not be support.")
    }
}

val HapticStyle.value: ImpactFeedbackStyle
    get() = when (this) {
        HapticStyle.SOFT -> ImpactFeedbackStyle.ImpactFeedbackStyleSoft
        HapticStyle.HEAVY -> ImpactFeedbackStyle.ImpactFeedbackStyleHeavy
        HapticStyle.LIGHT -> ImpactFeedbackStyle.ImpactFeedbackStyleLight
        HapticStyle.RIGID -> ImpactFeedbackStyle.ImpactFeedbackStyleRigid
        HapticStyle.MEDIUM -> ImpactFeedbackStyle.ImpactFeedbackStyleMedium
    }

data class ImpactFeedbackStyle(val longArray: LongArray, val repeater: Int = -1) {
    constructor(milliseconds: Long) : this(longArrayOf(0, milliseconds), -1)

    companion object {
        val ImpactFeedbackStyleLight: ImpactFeedbackStyle = ImpactFeedbackStyle(25)
        val ImpactFeedbackStyleSoft: ImpactFeedbackStyle = ImpactFeedbackStyle(50)
        val ImpactFeedbackStyleMedium: ImpactFeedbackStyle = ImpactFeedbackStyle(75)
        val ImpactFeedbackStyleHeavy: ImpactFeedbackStyle = ImpactFeedbackStyle(125)
        val ImpactFeedbackStyleRigid: ImpactFeedbackStyle = ImpactFeedbackStyleMedium
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImpactFeedbackStyle

        if (!longArray.contentEquals(other.longArray)) return false
        if (repeater != other.repeater) return false

        return true
    }

    override fun hashCode(): Int {
        var result = longArray.contentHashCode()
        result = 31 * result + repeater
        return result
    }
}