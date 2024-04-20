package composes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun NavigationHeader(
    title: String,
    color: SurfaceColors = SurfaceColors.defaultNavigatorColors,
    trailing: (@Composable () -> Unit)? = null,
) {
    val navigator = LocalNavigator.currentOrThrow
    Column(Modifier.fillMaxSize().zIndex(2f)) {
        Surface {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color.surface.value)
                    .padding(horizontal = 12.dp)
                    .padding(top = 16.dp, bottom = 8.dp)
                    .statusBarsPadding()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.clickable(
                        onClick = { navigator.pop() }, // Navigate back pop
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = "Pop back",
                        tint = color.foreground.value,
                        modifier = Modifier.size(38.dp)
                    )

                    Text(
                        text = title,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = color.foreground.value
                    )
                }

                if (trailing !== null) trailing() else Spacer(Modifier.width(1.dp))
            }
        }
    }
}