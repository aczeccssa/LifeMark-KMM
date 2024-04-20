package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import composes.ColorSet
import composes.SurfaceColors
import data.interfaces.Platform
import data.interfaces.IOSPlatform

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun ScreenSizeInfo.Companion.getScreenInfo(): ScreenSizeInfo {
    val density = LocalDensity.current
    val config = LocalWindowInfo.current.containerSize

    return remember(density, config) {
        ScreenSizeInfo(
            IntSize(config.width, config.height),
            DpSize(
                width = with(density) { config.width.toDp() },
                height = with(density) { config.height.toDp() }
            ),
        )
    }
}

actual val SpecificConfiguration.currentPlatform: Platform
    get() = IOSPlatform()

actual val ExperimentalSpecificComponentsConfiguration.Companion.default: ExperimentalSpecificComponentsConfiguration
    get() {
        val swiftPrimaryColor = Color(0xFFF05138)
        return ExperimentalSpecificComponentsConfiguration(
            platform = SpecificConfiguration.currentPlatform,
            surface = SurfaceColors(
                foreground = Color.White,
                surface = swiftPrimaryColor,
                background = swiftPrimaryColor
            ),
            primaryColor = ColorSet(swiftPrimaryColor)
        )
    }