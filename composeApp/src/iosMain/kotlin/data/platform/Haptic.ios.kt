package data.platform

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

private val HapticStyle.value: UIImpactFeedbackStyle
    get() = when (this) {
        HapticStyle.SOFT -> UIImpactFeedbackStyle.UIImpactFeedbackStyleSoft
        HapticStyle.HEAVY -> UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy
        HapticStyle.LIGHT -> UIImpactFeedbackStyle.UIImpactFeedbackStyleLight
        HapticStyle.RIGID -> UIImpactFeedbackStyle.UIImpactFeedbackStyleRigid
        HapticStyle.MEDIUM -> UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium
    }

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Haptic {
    actual fun playHapticPattern(style: HapticStyle) {
        val generatorVibrate = UIImpactFeedbackGenerator(style.value)
        generatorVibrate.prepare()
        generatorVibrate.impactOccurred()
    }

//    private var hapticEngine: CHHapticEngine? = null

//    init {
//        createHapticEngine()
//        startHapticEngine()
//    }

//    private fun createHapticEngine() {
//        try {
//            val hapticEngine = CHHapticEngine()
//            this.hapticEngine = hapticEngine
//        } catch (e: Exception) {
//            Napier.e(e.message ?: "Error creating haptic engine.", e)
//        }
//    }

//    private fun startHapticEngine() {
//        hapticEngine?.startWithCompletionHandler { error ->
//            error?.also {
//                val exception = it.toCodableException()
//                Napier.e(exception.message, exception, HAPTIC_TAG)
//            }
//        }
//    }

//    @OptIn(ExperimentalForeignApi::class)
//    private fun makeHapticPattern(): CHHapticPattern? {
//        val intensity = CHHapticEventParameter(CHHapticEventParameterIDHapticIntensity, 1F)
//        val sharpness = CHHapticEventParameter(CHHapticEventParameterIDHapticSharpness, 1F)
//
//        val event =
//            CHHapticEvent(CHHapticEventTypeHapticTransient, listOf(intensity, sharpness), 0.0)
//
//        try {
//            val pattern = CHHapticPattern(
//                events = listOf(event),
//                parameterCurves = emptyList<Any>(),
//                error = null
//            )
//            return pattern
//        } catch (e: Exception) {
//            Napier.e(e.message ?: "Error creating haptic pattern.", e, HAPTIC_TAG)
//            return null
//        }
//    }

//    @OptIn(ExperimentalForeignApi::class)
//    actual fun playHapticPattern(style: HapticStyle) {
//        if (!hapticEngine.capabilitiesForHardware().supportsHaptics) {
//            Napier.e("Haptics not supported on this device.", tag = HAPTIC_TAG)
//            return
//        }
//
//        val pattern = makeHapticPattern()
//        pattern?.also {
//            try {
//                val player = hapticEngine?.createPlayerWithPattern(it, error = null)
//                player?.startAtTime(0.0, null)
//            } catch (e: Exception) {
//                Napier.e("Error playing haptic pattern.", e, HAPTIC_TAG)
//            }
//        }
//    }
}