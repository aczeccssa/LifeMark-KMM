package screens.experimental

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import components.NavigationHeader
import data.ExperimentalSpecificComponentsConfiguration
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import data.default
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object ExperimentalComponentsScreen : Screen {
    @Composable
    override fun Content() {
        val textStyle = TextStyle(
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onBackground
        )
        Render(textStyle = textStyle)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun Render(
    pixelSize: IntSize = SpecificConfiguration.localScreenConfiguration.nativeBounds,
    renderSize: DpSize = SpecificConfiguration.localScreenConfiguration.bounds,
    textStyle: TextStyle,
) {
    val config = ExperimentalSpecificComponentsConfiguration.default
    val topOffset = NavigationHeaderConfiguration.defaultConfiguration.headerHeight + 28.dp

    Surface {
        NavigationHeader(
            "Component specific", NavigationHeaderConfiguration(color = config.surface)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().background(config.primaryColor.value)
                .safeContentPadding().padding(top = topOffset)
        ) {
            Image(
                painter = painterResource(config.platform.logo),
                contentDescription = "${config.platform.name}_logo",
                modifier = Modifier.height(140.dp)
            )

            Spacer(Modifier.height(12.dp))

            val apiTitle = config.platform.name + " " + config.platform.version
            Text(apiTitle, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = textStyle.color)

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Native: ${pixelSize.width}px x ${pixelSize.height}px", style = textStyle
            )
            Text(
                text = "Render: ${renderSize.width.value.toInt()}dp x ${renderSize.height.value.toInt()}dp",
                style = textStyle
            )
        }
    }
}