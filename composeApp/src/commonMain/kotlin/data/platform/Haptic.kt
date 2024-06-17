package data.platform

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object Haptic {
    fun playHapticPattern(style: HapticStyle = HapticStyle.MEDIUM)
}

enum class HapticStyle {
    SOFT, HEAVY, LIGHT, RIGID, MEDIUM;
}