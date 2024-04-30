package screens

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import composes.NavigationHeader
import data.NavigationHeaderConfiguration
import kotlin.math.max
import kotlin.math.min

object ExperimentalAnimate : Screen {
    @Composable
    override fun Content() {
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.headerHeight + 28.dp

        Surface {
            NavigationHeader("Animations")

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                    .safeContentPadding().padding(top = topOffset)
            ) {
                var isVisible by remember { mutableStateOf(false) }
                var isRound by remember { mutableStateOf(false) }
                Button(onClick = {
                    isVisible = !isVisible
                    isRound = !isRound
                }) {
                    Text(text = "Toggle")
                }
                val borderRadius by animateIntAsState(targetValue = if (isRound) 20 else 0,
                    /**
                     *spring(
                     *    dampingRatio = Spring.DampingRatioHighBouncy,
                     *    stiffness = Spring.StiffnessVeryLow
                     *)
                     */
                    animationSpec = keyframes {

                    }
                )
                Box(
                    modifier = Modifier.size(200.dp)
                        .clip(RoundedCornerShape(min(max(0, borderRadius), 100)))
                        .background(Color.Red)
                )
            }
        }
    }
}