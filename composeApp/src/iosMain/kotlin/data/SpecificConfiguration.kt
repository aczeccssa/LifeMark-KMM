package data

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntSize

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun LocalScreenConfiguration(): IntSize {
    val screenCfg = LocalWindowInfo.current.containerSize
    return IntSize(screenCfg.width, screenCfg.height)
}