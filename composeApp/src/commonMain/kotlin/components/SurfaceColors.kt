package components

import androidx.compose.ui.graphics.Color

data class SurfaceColors(
    var foreground: ColorSet,
    var surface: ColorSet,
    var background: ColorSet,
) {
    constructor(foreground: Color, surface: Color, background: Color) : this(
        ColorSet(foreground), ColorSet(surface), ColorSet(background)
    )

    companion object {
        val defaultNavigatorColors: SurfaceColors
            get() = SurfaceColors(
                foreground = ColorAssets.ForegroundColor,
                surface = ColorAssets.Surface,
                background = ColorAssets.Background
            )
    }
}