package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.ColorAssets
import components.ListItem
import components.NavigationHeader
import components.SurfaceColors
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import screens.experimental.ExperimentalComponentsScreen
import screens.experimental.ExperimentalMarkDownScreen

object ExperimentalFunListScreen : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        val navigator = LocalNavigator.currentOrThrow
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight

        Surface {
            NavigationHeader(title = "Experimental functions")

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                    .padding(top = topOffset)
            ) {
                ListItem(
                    Icons.Rounded.ThumbUp, MaterialTheme.colors.secondary, "Specific Platform"
                ) { navigator.push(ExperimentalComponentsScreen) }

                ListItem(
                    Icons.Rounded.Clear, MaterialTheme.colors.error, "Markdown && WebView"
                ) { navigator.push(ExperimentalMarkDownScreen) }
            }
        }
    }
}

