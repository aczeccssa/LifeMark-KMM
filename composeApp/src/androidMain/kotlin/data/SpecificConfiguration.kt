package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import components.ColorSet
import components.SurfaceColors
import data.interfaces.AndroidPlatform
import data.interfaces.Platform

@Composable
actual fun ScreenSizeInfo.Companion.getScreenInfo(): ScreenSizeInfo {
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val screenWidthDp = config.screenWidthDp.dp
    val screenHeightDp = config.screenHeightDp.dp

    return remember(density, config) {
        ScreenSizeInfo(
            IntSize(with(density) { screenWidthDp.roundToPx() }, with(density) { screenHeightDp.roundToPx() }),
            DpSize(screenWidthDp, screenHeightDp)
        )
    }
}

actual val SpecificConfiguration.currentPlatform: Platform
    get() = AndroidPlatform()

actual val ExperimentalSpecificComponentsConfiguration.Companion.default: ExperimentalSpecificComponentsConfiguration
    get() {
        val androidPrimaryColor = Color(0xFFA4C639)
        return ExperimentalSpecificComponentsConfiguration(
            platform = SpecificConfiguration.currentPlatform,
            surface = SurfaceColors(
                foreground = Color.White,
                surface = androidPrimaryColor,
                background = androidPrimaryColor
            ),
            primaryColor = ColorSet(androidPrimaryColor)
        )
    }