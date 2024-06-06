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

    val LMPrimaryUnselected = ColorSet(LMPurple.default.copy(alpha = 0.2f), LMPurple.dark.copy(alpha = 0.4f))

    val LightGray = ColorSet(Color(0xFFEDEDED), Color(0xFF444444))

    val Gray = ColorSet(Color(0xFFAAAAAA), Color(0xFF777777))

    val DeepGray = ColorSet(Color(0xFF999999))


    // Traditional
    val Yellow = ColorSet(Color(0xFFF2D252))

    val YellowVariant = ColorSet(Color(0xFFF2BC6B))

    val Red = ColorSet(Color(0xFFF32B50))


    // Kinds
    val SurfaceShadow = ColorSet(Color(0xFFDDDDDD), Color(0xFF111111))

    val ThisShadow = ColorSet(Color(0xFFFFFFFF), Color(0xFF111111))

    // SK group
    object SK {
        val Glyph = ColorSet(Color(0xFF000000))

        val GlyphGray = ColorSet(Color(0xFF1d1d1f))

        val GlyphGraySecondary = ColorSet(Color(0xFF6e6e73))

        val GlyphGraySecondaryAlt = ColorSet(Color(0xFF424245))

        val GlyphGrayTertiary = ColorSet(Color(0xFF86868b))

        val GlyphBlue = ColorSet(Color(0xFF0066cc))

        val GlyphOrange = ColorSet(Color(0xFFb64400))

        val GlyphGreen = ColorSet(Color(0xFF008009))

        val GlyphRed = ColorSet(Color(0xFFe30000))

        val Fill = ColorSet(Color(0xFFffffff))

        val FillSecondary = ColorSet(Color(0xFFfafafc))

        val FillTertiary = ColorSet(Color(0xFFf5f5f7))

        val FillGray = ColorSet(Color(0xFF1d1d1f))

        val FillGraySecondary = ColorSet(Color(0xFF86868b))

        val FillGrayTertiary = ColorSet(Color(0xFFd2d2d7))

        val FillGrayQuaternary = ColorSet(Color(0xFFe8e8ed))

        val FillBlue = ColorSet(Color(0xFF0071e3))

        val FillOrange = ColorSet(Color(0xFFf56300))

        val FillOrangeSecondary = ColorSet(Color(0xFFfff9f4))

        val FillGreen = ColorSet(Color(0xFF03a10e))

        val FillGreenSecondary = ColorSet(Color(0xFFf5fff6))

        val FillRed = ColorSet(Color(0xFFe30000))

        val FillRedSecondary = ColorSet(Color(0xFFfff2f4))

        val FillYellow = ColorSet(Color(0xFFffe045))

        val FillYellowSecondary = ColorSet(Color(0xFFfffef2))

        val Produced = ColorSet(Color(0xFFaf1e2d))

        val EnvironGreen = ColorSet(Color(0xFF00d959))

        val EnvironNeutral = ColorSet(Color(0xFFe8e8ed))
    }
}

fun Color.contrastColor(): Color {
    val luminance = (0.299 * this.red + 0.587 * this.green + 0.114 * this.blue)
    return if (luminance > 0.5) Color.Black else Color.White
}


data class ColorSet(
    val default: Color,
    private val _dark: Color? = null
) {
    val value: Color @Composable get() = color()

    val dark: Color get() = _dark ?: default

    @Composable
    private fun color(): Color {
        return if (isSystemInDarkTheme()) _dark ?: default else default
    }
}

