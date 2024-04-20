package composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow

class SurfaceColors(
    val foreground: ColorSet,
    val surface: ColorSet,
    val background: ColorSet,
) {
    constructor(foreground: Color, surface: Color, background: Color) : this(
        ColorSet(foreground), ColorSet(surface), ColorSet(background)
    )

    companion object {
        val defaultNavigatorColors: SurfaceColors
            get() {
                return SurfaceColors(
                    ColorAssets.ForegroundColor,
                    ColorAssets.Surface,
                    ColorAssets.Background
                )
            }
    }
}

@Composable
fun Modifier.headSafePadding(): Modifier {
    return Modifier.padding(top = 76.dp)
}

data class NavigationView(
    val title: String,
    val trailing: (@Composable () -> Unit)? = null,
    val colors: SurfaceColors = SurfaceColors.defaultNavigatorColors,
    val content: @Composable (navigator: Navigator) -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Surface {
            NavigationHeader(title, colors, trailing)

            Box(Modifier.fillMaxSize().background(colors.background.value)) {
                content(navigator)
            }
        }
    }
}