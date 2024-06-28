package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.ColorAssets
import components.ListItem
import components.NavigationHeader
import components.SurfaceColors
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Archive
import compose.icons.evaicons.outline.Browser
import compose.icons.evaicons.outline.Grid
import compose.icons.evaicons.outline.Monitor
import data.NavigationHeaderConfiguration
import screens.experimental.ExperimentalComponentsScreen
import screens.experimental.ExperimentalGlobalSheepTestScreen
import screens.experimental.ExperimentalMarkDownScreen
import screens.experimental.ExperimentalPaperScreen

object ExperimentalFunListScreen : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        val navigator = LocalNavigator.currentOrThrow
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight

        Surface {
            NavigationHeader(
                title = "Experimental functions",
                configuration = NavigationHeaderConfiguration.defaultConfiguration.copy(
                    color = SurfaceColors.defaultNavigatorColors.copy(
                        surface = ColorAssets.Background
                    )
                )
            )

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                    .background(MaterialTheme.colors.background).padding(top = topOffset)
                    .padding(bottom = NAVIGATION_BAR_HEIGHT)
            ) {
                ListItem(
                    imageVector = EvaIcons.Outline.Monitor,
                    tint = MaterialTheme.colors.secondary,
                    title = "Specific Platform",
                    sub = "Experimental functions for specific platforms"
                ) { navigator.push(ExperimentalComponentsScreen) }

                ListItem(
                    imageVector = EvaIcons.Outline.Browser,
                    tint = MaterialTheme.colors.error,
                    title = "Markdown && WebView",
                    sub = "Experimental functions for Markdown and WebView"
                ) { navigator.push(ExperimentalMarkDownScreen) }

                ListItem(
                    imageVector = EvaIcons.Outline.Archive,
                    tint = MaterialTheme.colors.secondary,
                    title = "Paper T",
                    sub = "Experimental functions for Paper style."
                ) { navigator.push(ExperimentalPaperScreen) }

                ListItem(
                    imageVector = EvaIcons.Outline.Grid,
                    tint = ColorAssets.SK.FillYellowSecondary.value,
                    title = "Global Sheep",
                    sub = "Experimental global sheep feature."
                ) { navigator.push(ExperimentalGlobalSheepTestScreen) }

            }
        }
    }
}

