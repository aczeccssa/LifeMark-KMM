package screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.kotlin_full_color_logo_mush_rgb
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import screens.MainApplicationNavigator

object SplashScreen : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            delay(2000) // Wait for 2 seconds
            navigator.replaceAll(MainApplicationNavigator)
        }

        Surface {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
            ) {
                Image(
                    painter = painterResource(Res.drawable.kotlin_full_color_logo_mush_rgb),
                    contentDescription = "kotlin_logo_full_color",
                    modifier = Modifier.width(160.dp)
                )
            }
        }
    }
}