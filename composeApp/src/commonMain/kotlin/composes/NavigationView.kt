package composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen

class SurfaceColors(
    val foreground: ColorSet,
    val background: ColorSet
) {
    constructor(foreground: Color, background: Color) : this(
        ColorSet(foreground), ColorSet(background)
    )

    companion object {
        val defaultNavigatorColors: SurfaceColors
            get() {
                return SurfaceColors(
                    ColorAssets.ForegroundColor,
                    ColorAssets.Background
                )
            }
    }
}

data class NavigationView(
    val title: String,
    val trailing: (@Composable () -> Unit)? = null,
    val colors: SurfaceColors = SurfaceColors.defaultNavigatorColors,
    val content: @Composable () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        Surface {
            NavigationHeader(title, trailing)
            
            Box(Modifier.fillMaxSize().background(colors.background.value)) {
                content()
            }
        }
    }
}