package data.platform

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Haptic {
    actual fun playHapticPattern(style: HapticStyle) {
        val generatorVibrate = UIImpactFeedbackGenerator(style.value)
        generatorVibrate.prepare()
        generatorVibrate.impactOccurred()
    }
}

private val HapticStyle.value: UIImpactFeedbackStyle
    get() = when (this) {
        HapticStyle.SOFT -> UIImpactFeedbackStyle.UIImpactFeedbackStyleSoft
        HapticStyle.HEAVY -> UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy
        HapticStyle.LIGHT -> UIImpactFeedbackStyle.UIImpactFeedbackStyleLight
        HapticStyle.RIGID -> UIImpactFeedbackStyle.UIImpactFeedbackStyleRigid
        HapticStyle.MEDIUM -> UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium
    }