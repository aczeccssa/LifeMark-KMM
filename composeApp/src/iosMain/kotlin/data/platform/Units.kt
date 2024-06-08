package data.platform

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.UIKit.UIImage

typealias ImageBytes = NSData

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun ByteArray.toImageBytes(): ImageBytes? = memScoped {
    val string = NSString.create(string = this@toImageBytes.decodeToString())
    return string.dataUsingEncoding(NSUTF8StringEncoding)
}

fun ByteArray.toUIImage(): UIImage? {
    return this.toImageBytes()?.let { return UIImage(data = it) }
}