package data.platform

import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.compose.ui.graphics.asAndroidBitmap
import cache.AndroidContents
import com.preat.peekaboo.image.picker.toImageBitmap
import data.units.CodableException
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object MediaManageStore {
    @Throws(CodableException::class)
    actual fun storageImageToPhotoLibrary(image: ByteArray, title: String?, desc: String?) {
        val context = AndroidContents.localContext
        val mResolver = context?.contentResolver
        MediaStore.Images.Media.insertImage(
            mResolver,
            image.toImageBitmap().asAndroidBitmap(),
            title,
            desc
        )
    }
}

@OptIn(ExperimentalEncodingApi::class)
actual fun ByteArray.toDataUrl(): String? {
    val bitmap: Bitmap = this.toImageBitmap().asAndroidBitmap()
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
    return "data:image/png;base64," + Base64.encode(byteArray)
}