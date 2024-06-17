package data.platform

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object MediaManageStore {
    fun storageImageToPhotoLibrary(image: ByteArray, title: String? = null, desc: String? = null)
}

val MediaManageStore.TAG get() = "MediaStorage"

expect fun ByteArray.toDataUrl(): String?