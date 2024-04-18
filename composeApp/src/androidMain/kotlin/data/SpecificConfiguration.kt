package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

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