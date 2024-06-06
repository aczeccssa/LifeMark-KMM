package screens.experimental

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import components.NavigationHeader
import components.contrastColor
import data.NavigationHeaderConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

object ColorPreviewer : Screen {
    @Composable
    @Preview
    override fun Content() {
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.headerHeight + 28.dp

        Surface {
            NavigationHeader("Color preview")

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .safeContentPadding()
                    .padding(top = topOffset)
            ) {
                ColorCard("primary", MaterialTheme.colors.primary)
                ColorCard("primaryVariant", MaterialTheme.colors.primaryVariant)
                ColorCard("secondary", MaterialTheme.colors.secondary)
                ColorCard("secondaryVariant", MaterialTheme.colors.secondaryVariant)
                ColorCard("background", MaterialTheme.colors.background)
                ColorCard("surface", MaterialTheme.colors.surface)
                ColorCard("error", MaterialTheme.colors.error)
                ColorCard("onPrimary", MaterialTheme.colors.onPrimary)
                ColorCard("onSecondary", MaterialTheme.colors.onSecondary)
                ColorCard("onBackground", MaterialTheme.colors.onBackground)
                ColorCard("onSurface", MaterialTheme.colors.onSurface)
                ColorCard("onError", MaterialTheme.colors.onError)
            }
        }
    }

    @Composable
    private fun ColorCard(name: String, color: Color) {
        val boxHeight = 64.dp
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .height(boxHeight).fillMaxWidth().clip(RoundedCornerShape(8.dp))
                .background(color)
        ) { Text(name, color = color.contrastColor()) }
    }
}
