package components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ColorAssets {
    // BStruct
    val ForegroundColor = ColorSet(Color(0xFF1D1D1F), Color(0xFFFFFFFF))

    val Surface = ColorSet(Color(0xFFFFFFFF), Color(0xFF28282E))

    val Background = ColorSet(Color(0xFFFAFAFA), Color(0xFF141414))


    // Themes
    val LMPurple = ColorSet(Color(0xFF383FC4), Color(0xFF535BF2))

    val LMPurpleVariant = ColorSet(Color(0xFF747BFF), Color(0xFF434ACB))


    // Traditional
    val Yellow = ColorSet(Color(0xFFF2D252))

    val YellowVariant = ColorSet(Color(0xFFF2BC6B))

    val Red = ColorSet(Color(0xFFF32B50))


    // Kinds
    val SurfaceShadow = ColorSet(Color(0xFFDDDDDD), Color(0xFF111111))

    val ThisShadow = ColorSet(Color(0xFFFFFFFF), Color(0xFF111111))
}

fun Color.contrastColor(): Color {
    val luminance = (0.299 * this.red + 0.587 * this.green + 0.114 * this.blue)
    return if (luminance > 0.5) Color.Black else Color.White
}


data class ColorSet(
    val default: Color,
    private val _dark: Color? = null
) {
    val value: Color
        @Composable
        get() = color()

    val dark: Color
        get() = _dark ?: default

    @Composable
    private fun color(): Color {
        return if (isSystemInDarkTheme()) {
            _dark ?: default
        } else default
    }
}
