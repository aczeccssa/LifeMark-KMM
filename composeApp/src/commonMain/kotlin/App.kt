import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import composes.LifeMarkTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.splash.SplashScreen

// Supported functions:
//   1. ktor.serialization
//   2. ktor.httpclient
//   3. kamel.image
//   4. voyager.navigator : `https://voyager.adriel.cafe`
@Composable
@Preview
fun App() {
    LifeMarkTheme { // Custom Material Theme.
        Navigator(SplashScreen) { navigator ->
            /**
             * `ScaleTransition`: Scale size and color opacity transition.
             * `FadeTransition`: Only opacity transition.
             * `SlideTransition`: Slide start to end transition.
             * Can enable custom transition in -> `https://voyager.adriel.cafe/transitions`.
             */
            ScaleTransition(navigator, animationSpec = spring(stiffness = Spring.StiffnessLow))
        }
    }
}