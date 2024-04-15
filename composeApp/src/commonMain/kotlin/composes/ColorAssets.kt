package composes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ColorAssets {
    val ForegroundColor = ColorSet(Color(0xFF1D1D1F), Color(0xFFFFFFFF))

    val PrimaryColor = ColorSet(Color(0xFF383FC4), Color(0xFF535BF2))

    val PrimaryVariantColor = ColorSet(Color(0xFF747BFF), Color(0xFF434ACB))

    val SecondaryColor = ColorSet(Color(0xFFF2D252))

    val SecondaryVariantColor = ColorSet(Color(0xFFF2BC6B))

    val SurfaceColor = ColorSet(Color(0xFFFFFFFF), Color(0xFF28282E)) // light: ; dark: 1D1D1F

    val BackgroundColor = ColorSet(Color(0xFFFAFAFA), Color(0xFF141414)) // light: FCFCFC; dark: 
    
    val ErrorColor = ColorSet(Color(0xFFF32B50))
    
    val SurfaceShadowColor = ColorSet(Color(0xFF000000).copy(alpha = 0.1f), Color(0xFF111111))
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

@Composable
fun LifeMarkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    
    println("Current theme mode: ${if (darkTheme) "Dark" else "Light"}")

    MaterialTheme(
        colors = colors, // Colors
        // typography = Typography, // Font Family
        // shapes = Shapes, // Shape
        content = content // Asign Graphic View
    )
}

private val DarkColorPalette = darkColors(
    primary = ColorAssets.PrimaryColor.dark,
    primaryVariant = ColorAssets.PrimaryVariantColor.dark,
    secondary = ColorAssets.SecondaryColor.dark,
    secondaryVariant = ColorAssets.SecondaryVariantColor.dark,
    background = ColorAssets.BackgroundColor.dark,
    surface = ColorAssets.SurfaceColor.dark,
    error = ColorAssets.ErrorColor.dark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = ColorAssets.ForegroundColor.dark,
    onSurface = ColorAssets.ForegroundColor.dark,
    onError = Color.White
)

private val LightColorPalette = lightColors(
    primary = ColorAssets.PrimaryColor.default,
    primaryVariant = ColorAssets.PrimaryVariantColor.default,
    secondary = ColorAssets.SecondaryColor.default,
    secondaryVariant = ColorAssets.SecondaryVariantColor.default,
    background = ColorAssets.BackgroundColor.default,
    surface = ColorAssets.SurfaceColor.default,
    error = ColorAssets.ErrorColor.default,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = ColorAssets.ForegroundColor.default,
    onSurface = ColorAssets.ForegroundColor.default,
    onError = Color.White
)
