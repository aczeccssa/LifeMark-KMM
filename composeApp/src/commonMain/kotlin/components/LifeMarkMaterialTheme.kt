package components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import data.resources.Poppins

@Composable
fun LifeMarkMaterialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    println("Current theme mode: ${if (darkTheme) "Dark" else "Light"}")

    MaterialTheme(
        colors = colors, // Colors
        typography = LifeMarkTypography, // Font Family
        shapes = Shapes(), // Shape
        content = content // Assign Graphic View
    )
}

private val DarkColorPalette = darkColors(
    primary = ColorAssets.LMPurple.dark,
    primaryVariant = ColorAssets.LMPurpleVariant.dark,
    secondary = ColorAssets.Yellow.dark,
    secondaryVariant = ColorAssets.YellowVariant.dark,
    background = ColorAssets.Background.dark,
    surface = ColorAssets.Surface.dark,
    error = ColorAssets.Red.dark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = ColorAssets.ForegroundColor.dark,
    onSurface = ColorAssets.ForegroundColor.dark,
    onError = Color.White
)

private val LightColorPalette = lightColors(
    primary = ColorAssets.LMPurple.default,
    primaryVariant = ColorAssets.LMPurpleVariant.default,
    secondary = ColorAssets.Yellow.default,
    secondaryVariant = ColorAssets.YellowVariant.default,
    background = ColorAssets.Background.default,
    surface = ColorAssets.Surface.default,
    error = ColorAssets.Red.default,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = ColorAssets.ForegroundColor.default,
    onSurface = ColorAssets.ForegroundColor.default,
    onError = Color.White
)

private val LifeMarkTypography
    @Composable get() = Typography().copy(
        h1 = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 96.sp,
            lineHeight = 112.sp,
            letterSpacing = (-1.5).sp
        ),
        h2 = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 60.sp,
            lineHeight = 72.sp,
            letterSpacing = (-0.5).sp
        ),
        h3 = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 48.sp,
            lineHeight = 56.sp,
            letterSpacing = 0.sp
        ),
        h4 = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 34.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.25.sp
        ),
        h5 = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 24.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        h6 = TextStyle(
            fontFamily = Poppins.medium.toFontFamily(),
            fontSize = 20.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        subtitle1 = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        subtitle2 = TextStyle(
            fontFamily = Poppins.medium.toFontFamily(),
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.1.sp
        ),
        body1 = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        body2 = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        button = TextStyle(
            fontFamily = Poppins.medium.toFontFamily(),
            fontSize = 14.sp,
            lineHeight = 16.sp,
            letterSpacing = 1.25.sp
        ),
        caption = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        overline = TextStyle(
            fontFamily = Poppins.regular.toFontFamily(),
            fontSize = 10.sp,
            lineHeight = 16.sp,
            letterSpacing = 1.5.sp
        ),
    )