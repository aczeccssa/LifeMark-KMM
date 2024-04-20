package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import composes.NavigationHeader
import data.ExperimentalSpecificComponentsConfiguration
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
            color = MaterialTheme.colors.primary
        )
        ComponentA(textStyle = textStyle)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ComponentA(
    pixelSize: IntSize = SpecificConfiguration.localScreenConfiguration.nativeBounds,
    renderSize: DpSize = SpecificConfiguration.localScreenConfiguration.bounds,
    textStyle: TextStyle,
) {
    val config = ExperimentalSpecificComponentsConfiguration.default

    Surface {
        NavigationHeader("Component specific", config.surface)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(config.primaryColor.value)
                .safeContentPadding()
        ) {
            Image(
                painter = painterResource(config.platform.logo),
                contentDescription = "${config.platform.name}_logo",
                modifier = Modifier.height(140.dp)
            )

            Spacer(Modifier.height(12.dp))

            val apiTitle = config.platform.name + " " + config.platform.version
            Text(apiTitle, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = textStyle.color)

            Spacer(Modifier.height(12.dp))

            Text("Native: ${pixelSize.width}px x ${pixelSize.height}px", style = textStyle)
            Text("Render: ${renderSize.width} x ${renderSize.height}", style = textStyle)
        }
    }
}