@file:Suppress("UNREACHABLE_CODE")

package data.platform

import data.units.CodableException
import data.units.now
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.datetime.LocalDateTime
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.UIKit.UIImage
import platform.UIKit.UIImageWriteToSavedPhotosAlbum

//@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
//actual object MediaStorage {
//    @OptIn(ExperimentalForeignApi::class)
//    @Throws(CodableException::class)
//    actual fun storageImageToPhotoLibrary(image: ByteArray, title: String?, desc: String?) {
//        try {
//            val toSavedImage = image.toUIImage()
//            toSavedImage?.let {
//                UIImageWriteToSavedPhotosAlbum(toSavedImage, null, null, null)
//            }
//        } catch (e: Error) {
//            println("${LocalDateTime.now()} - Failed to save image to photo library")
//            throw CodableException(-1, e.message ?: "Unknown error")
//        }
//    }
//}
//
//typealias ImageBytes = NSData
//
//@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
//fun ByteArray.toImageBytes(): ImageBytes? = memScoped {
//    val string = NSString.create(string = this@toImageBytes.decodeToString())
//    return string.dataUsingEncoding(NSUTF8StringEncoding)
//}
//
//fun ByteArray.toUIImage(): UIImage? {
//    return this.toImageBytes()?.let { return UIImage(data = it) }
//}