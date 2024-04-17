package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import composes.IconListItem
import composes.ListView
import composes.NavigationHeader

object ExperimentalFunListScreen : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        val navigator = LocalNavigator.currentOrThrow

        Surface {
            NavigationHeader("Experimental functions")

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .verticalScroll(scrollState)
                    .safeContentPadding()
                    .padding(top = 90.dp)
            ) {
                ListView {
                    IconListItem(Icons.Rounded.Done, "Material colors", MaterialTheme.colors.secondary) {
                        navigator.push(ColorPreviewer)
                    }

                    IconListItem(Icons.Rounded.LocationOn, "About LifeMark 2024", MaterialTheme.colors.primary) {
                        navigator.push(InfoScreen)
                    }
                }
            }
        }
    }
}