package composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class NavigatorColors(
    val foreground: ColorSet,
    val background: ColorSet
) {
    companion object {
        val defaultNavigatorColors: NavigatorColors
            get() {
                return NavigatorColors(
                    ColorAssets.ForegroundColor,
                    ColorAssets.BackgroundColor
                )
            }
    }
}

data class NavigationView(
    val title: String,
    val trailing: (@Composable () -> Unit)? = null,
    val colors: NavigatorColors = NavigatorColors.defaultNavigatorColors,
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