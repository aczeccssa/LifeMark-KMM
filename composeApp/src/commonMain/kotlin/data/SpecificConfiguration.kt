package data

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

/**
 * ### Head offset model actual sampling
 *
 * ----------------------------------------------------------------------------------------
| **Sys** |   **Device**    |**require(dp)**| **header(dp)** |**Pixel(px)**|**Render(dp)**|
| :------ | :-------------: | :-----------: | :------------: | :---------: | :----------: |
| iOS     | iPhone 11       |      72       |       --       | 1792 x 828  |  986 x 414   |
| iOS     | iPad Pro 11inch |      70       |       --       | 2778 x 1940 |  1389 x 970  |
| Andriod | Xiaomi 12 Pro   |      70       |       50       | 3035 x 1439 |  867 x 411   |
| Andriod | Pixel 8 Pro     |      82       |       62       | 2992 x 1344 |  973 x 448   |
 * ----------------------------------------------------------------------------------------
 */
data class NavigationHeaderConfiguration(
    val iconSize: Dp = 38.dp,
    val padding: PaddingValues = PaddingValues(12.dp, 16.dp, 12.dp, 8.dp),
    val color: SurfaceColors = SurfaceColors.defaultNavigatorColors,
    val text: TextStyle = TextStyle.Default
) {
    companion object {
        val defaultConfiguration: NavigationHeaderConfiguration
            @Composable
            get() = NavigationHeaderConfiguration(
                iconSize = 38.dp,
                padding = PaddingValues(12.dp, 16.dp, 12.dp, 8.dp),
                color = SurfaceColors.defaultNavigatorColors,
                text = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = SurfaceColors.defaultNavigatorColors.foreground.value
                )
            )
    }

    /**
     * ### To get device status bar height:
     * #### 1. Use object `WindowsInsets`
     * ```kotlin
     * WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
     * ```
     * #### 2. Refer to Jetpack/composite-multiplatform [issure#4049](https://github.com/JetBrains/compose-multiplatform/discussions/4049#discussioncomment-8195448)
     * ```kotlin
     * val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
     * val screenBounds: Rectangle = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
     * val statusBarHeight = screenBounds.height - screenSize.height
     * ```
     * #### 3. Review in Jetbrains/compose-multiplatform [issure#4049](https://github.com/JetBrains/compose-multiplatform/discussions/4049#discussioncomment-8567780)
     */
    private val statusBarPadding: Dp
        @Composable get() = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val calculateHeight: Dp
        @Composable
        get() = iconSize + statusBarPadding + padding.calculateTopPadding() + padding.calculateBottomPadding()

    val headerHeight: Dp = iconSize + padding.calculateTopPadding() + padding.calculateBottomPadding()
}