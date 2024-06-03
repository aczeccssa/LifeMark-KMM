package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import data.NavigationHeaderConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun NavigationHeader(
    title: String,
    configuration: NavigationHeaderConfiguration = NavigationHeaderConfiguration.defaultConfiguration,
    trailing: (@Composable () -> Unit)? = null,
) {
    val navigator = LocalNavigator.currentOrThrow

    Column(Modifier.fillMaxSize().zIndex(2f)) {
        Surface {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .clickable(
                        onClick = { navigator.pop() }, // Navigate back pop
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .fillMaxWidth().height(configuration.calculateHeight)
                    .background(configuration.color.surface.value)
                    .padding(horizontal = 12.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
                    .statusBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = "Pop back",
                    tint = configuration.color.foreground.value,
                    modifier = Modifier.size(38.dp)
                )

                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = configuration.color.foreground.value
                )

                Spacer(Modifier.weight(1f))

                trailing?.let { it() }
            }
        }
    }
}