package data

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

object SpecificConfiguration {
    val localScreenConfiguration: ScreenSizeInfo
        @Composable
        get() = ScreenSizeInfo.getScreenInfo()
}

/** Getting screen size info for UI-related calculations */
data class ScreenSizeInfo(val nativeBounds: IntSize, val bounds: DpSize) {
    companion object
}

@Composable
expect fun ScreenSizeInfo.Companion.getScreenInfo(): ScreenSizeInfo