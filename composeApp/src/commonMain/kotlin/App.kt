import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.HomeScreen

// Supported functions:
//   1. ktor.serialization
//   2. ktor.okhttpclient
//   3. kamel.image
//   4. voyager.navigator
@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(HomeScreen) { navigator ->
            /**
             * `ScaleTransition`: Scale size and color opacity transition.
             * `FadeTransition`: Only opacity transition.
             * `SlideTransition`: Slide start to end transition.
             * Can enable custom transition in -> `https://voyager.adriel.cafe/transitions`.
             */
            SlideTransition(navigator) // Enable slide animation.
        }
    }
}