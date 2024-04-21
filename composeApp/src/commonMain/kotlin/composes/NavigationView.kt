package composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.NavigationHeaderConfiguration

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

data class NavigationView(
    val title: String,
    val trailing: (@Composable () -> Unit)? = null,
    val content: @Composable (navigator: Navigator) -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val configuration: NavigationHeaderConfiguration = NavigationHeaderConfiguration.defaultConfiguration
        val navigator = LocalNavigator.currentOrThrow
        Surface {
            NavigationHeader(title, configuration, trailing)

            Box(Modifier.fillMaxSize().background(configuration.color.background.value)) {
                content(navigator)
            }
        }
    }
}