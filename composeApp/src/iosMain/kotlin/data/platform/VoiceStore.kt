package data.platform

import platform.AudioToolbox.AudioServicesPlaySystemSoundWithCompletion

val VoiceStoreStyle.id: UInt
    get() = when (this) {
        VoiceStoreStyle.CLICK -> 1007u
        VoiceStoreStyle.XIU -> 1000u
        VoiceStoreStyle.CONNECTION_FAILED -> 1310u
        VoiceStoreStyle.DISCONNECTED -> 1311u
        VoiceStoreStyle.FAILED -> 1312u
        VoiceStoreStyle.SUCCESS -> 1313u
    }

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object VoiceStore {
    actual fun play(style: VoiceStoreStyle, inCompletionBlock: (() -> Unit)?) {
        val systemSound = style.id
        AudioServicesPlaySystemSoundWithCompletion(systemSound, inCompletionBlock)
    }
}