package data.platform

import data.units.CodableException
import data.units.now
import io.github.aakira.napier.Napier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.datetime.LocalDateTime
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImageWriteToSavedPhotosAlbum

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object MediaStorage {
    @OptIn(ExperimentalForeignApi::class)
    @Throws(CodableException::class)
    actual fun storageImageToPhotoLibrary(image: ByteArray, title: String?, desc: String?) {
        try {
            val toSavedImage = image.toUIImage()
            toSavedImage?.also {
                UIImageWriteToSavedPhotosAlbum(it, null, null, null)
            }
        } catch (e: Error) {
            Napier.e("${LocalDateTime.now()} - Failed to save image to photo library", e, this.TAG)
            throw CodableException(-1, e.message ?: "Unknown error")
        }
    }
}

actual fun ByteArray.toDataUrl(): String? {
    val uiImage = this.toUIImage()
    val imageData: NSData? = uiImage?.let { UIImageJPEGRepresentation(it, 1.0) }
    val base64ImageData: String? = imageData?.base64EncodedStringWithOptions(0u)
    return base64ImageData?.let { "data:image/jpeg;base64,$it" }
}