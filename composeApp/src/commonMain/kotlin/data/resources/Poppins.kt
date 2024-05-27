package data.resources

import androidx.compose.runtime.Composable
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.poppins_bold
import lifemark_kmm.composeapp.generated.resources.poppins_light_italic
import lifemark_kmm.composeapp.generated.resources.poppins_medium
import lifemark_kmm.composeapp.generated.resources.poppins_medium_italic
import lifemark_kmm.composeapp.generated.resources.poppins_regular
import lifemark_kmm.composeapp.generated.resources.poppins_regular_italic
import lifemark_kmm.composeapp.generated.resources.poppins_semibold
import lifemark_kmm.composeapp.generated.resources.poppins_semibold_italic
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

/**
 * Custom Poppins fonts
 *
 * Android Studio:
 *  * When your android studio alert this resource class's data error like resource not found please view the [issue](https://github.com/aczeccssa/LifeMark-KMM/issues/3)
 */
@OptIn(ExperimentalResourceApi::class)
object Poppins {
    val regular @Composable get() = Font(Res.font.poppins_regular)

    val medium @Composable get() = Font(Res.font.poppins_medium)

    val semiBold @Composable get() = Font(Res.font.poppins_semibold)

    val bold @Composable get() = Font(Res.font.poppins_bold)

    val lightItalic @Composable get() = Font(Res.font.poppins_light_italic)

    val mediumItalic @Composable get() = Font(Res.font.poppins_medium_italic)

    val regularItalic @Composable get() = Font(Res.font.poppins_regular_italic)

    val semiBoldItalic @Composable get() = Font(Res.font.poppins_semibold_italic)
}