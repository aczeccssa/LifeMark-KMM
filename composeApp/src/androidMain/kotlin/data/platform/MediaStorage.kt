@file:Suppress("DEPRECATION")

//package data.platform
//
//import android.provider.MediaStore
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.asAndroidBitmap
//import cache.AndroidContents
//import com.preat.peekaboo.image.picker.toImageBitmap
//import data.units.CodableException
//
//@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
//actual object MediaStorage {
//    @Throws(CodableException::class)
//    actual fun storageImageToPhotoLibrary(image: ByteArray, title: String?, desc: String?) {
//        val context = AndroidContents.localContext
//        val mResolver = context?.contentResolver
//        MediaStore.Images.Media.insertImage(mResolver, image.toImageBitmap().asAndroidBitmap(), title, desc)
//    }
//}