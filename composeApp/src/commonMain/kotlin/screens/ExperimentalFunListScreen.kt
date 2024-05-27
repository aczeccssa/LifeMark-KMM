package screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.IconListItem
import components.ListView
import components.NavigationHeader
import data.NavigationHeaderConfiguration
import screens.experimental.ExperimentalComponentsScreen
import screens.experimental.ExperimentalMarkDownScreen

object ExperimentalFunListScreen : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        val navigator = LocalNavigator.currentOrThrow
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.headerHeight + 28.dp

        Surface {
            NavigationHeader("Experimental functions")

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                    .verticalScroll(scrollState).safeContentPadding().padding(top = topOffset)
            ) {
                ListView {
                    IconListItem(
                        Icons.Rounded.LocationOn,
                        "About LifeMark 2024",
                        MaterialTheme.colors.primary
                    ) { navigator.push(InfoScreen) }

                    IconListItem(
                        Icons.Rounded.Clear, "Components", MaterialTheme.colors.error
                    ) { navigator.push(ExperimentalComponentsScreen) }

                    IconListItem(
                        Icons.Rounded.Clear, "Markdown && WebView", MaterialTheme.colors.error
                    ) { navigator.push(ExperimentalMarkDownScreen) }
                }
            }
        }
    }
}

