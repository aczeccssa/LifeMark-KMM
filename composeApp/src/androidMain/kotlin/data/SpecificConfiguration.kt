package data

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntSize

@Composable
actual fun LocalScreenConfiguration(): IntSize {
    val screenCfg = LocalConfiguration.current
    return IntSize(screenCfg.screenWidthDp, screenCfg.screenHeightDp)
}