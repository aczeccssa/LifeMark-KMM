package data

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import composes.ColorSet
import composes.SurfaceColors
import data.interfaces.Platform

object SpecificConfiguration {
    val localScreenConfiguration: ScreenSizeInfo
        @Composable
        get() = ScreenSizeInfo.getScreenInfo()
}

/** Getting screen size info for UI-related calculations */
data class ScreenSizeInfo(val nativeBounds: IntSize, val bounds: DpSize) {
    companion object

    val zoomRatio: Float
        get() {
            val widthRatio = nativeBounds.width / bounds.width.value
            val heightRatio = nativeBounds.height / bounds.height.value

            return if (widthRatio == heightRatio) widthRatio else 0f
        }

    /** Resolution width / height ratio */
    val aspectRatio: Float
        get() = nativeBounds.width.toFloat() / nativeBounds.height.toFloat()
}

@Composable
expect fun ScreenSizeInfo.Companion.getScreenInfo(): ScreenSizeInfo

expect val SpecificConfiguration.currentPlatform: Platform


/**  */
data class ExperimentalSpecificComponentsConfiguration(
    val platform: Platform,
    val surface: SurfaceColors,
    val primaryColor: ColorSet
) {
    companion object
}

expect val ExperimentalSpecificComponentsConfiguration.Companion.default: ExperimentalSpecificComponentsConfiguration

