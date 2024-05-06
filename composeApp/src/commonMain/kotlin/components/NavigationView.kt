package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        val scrollState = rememberScrollState()
        val navigator = LocalNavigator.currentOrThrow
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.headerHeight + 28.dp

        Surface {
            NavigationHeader("Experimental functions", configuration, trailing)

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .verticalScroll(scrollState)
                    .safeContentPadding().padding(top = topOffset)
            ) { content(navigator) }
        }
    }
}