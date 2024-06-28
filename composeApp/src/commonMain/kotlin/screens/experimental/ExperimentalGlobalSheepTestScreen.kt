package screens.experimental

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import components.NavigationHeader
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import kotlinx.coroutines.delay
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.kotlin_full_color_logo_mush_rgb
import org.jetbrains.compose.resources.painterResource
import screens.NAVIGATION_BAR_HEIGHT

object ExperimentalGlobalSheepTestScreen : Screen {
    @Composable
    override fun Content() {
        // This screen is just a placeholder
        var rotateYDegTarget by remember { mutableStateOf(0f) }
        val animateYValue = animateFloatAsState(rotateYDegTarget)


        LaunchedEffect(Unit) {
            while(true) {
                rotateYDegTarget += 1
                delay(10)
            }
        }

        // Properties
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight

        Surface {
            NavigationHeader(
                title = "Global Sheep",
                configuration = NavigationHeaderConfiguration.transparentConfiguration
            )

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding)
                    .padding(top = topOffset, bottom = NAVIGATION_BAR_HEIGHT),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.kotlin_full_color_logo_mush_rgb),
                    contentDescription = "kotlin_logo_full_color",
                    modifier = Modifier
                        .width(160.dp)
                        .scale(1f)
                        .graphicsLayer {
                            cameraDistance = 12f * density
                            rotationY = animateYValue.value
                            // rotationZ = 12f
                            // rotationX = 45f
                        }
                )
            }
        }
    }
}