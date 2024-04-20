package data.interfaces

import android.os.Build
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.android_logo_white
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

class AndroidPlatform : Platform {
    override val name: String = "Android"
    
    override val version: String = "${Build.VERSION.SDK_INT}"
    
    @OptIn(ExperimentalResourceApi::class)
    override val logo: DrawableResource = Res.drawable.android_logo_white
}
