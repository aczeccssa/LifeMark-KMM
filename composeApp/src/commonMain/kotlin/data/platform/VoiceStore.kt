package data.platform

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object VoiceStore {
    fun play(style: VoiceStoreStyle, inCompletionBlock: (() -> Unit)? = null)
}

enum class VoiceStoreStyle {
    CLICK, XIU, CONNECTION_FAILED, DISCONNECTED, FAILED, SUCCESS;
}