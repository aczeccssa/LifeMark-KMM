import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import components.LifeMarkMaterialTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.splash.SplashScreen

/**
 * ██╗    ██████╗███████╗███████╗ █████╗ ███╗     ███╗██████╗ ██╗ ██╗       ██████╗ ███████╗██╗    ██╗
 * ██║    ╚═██╔═╝██╔════╝██╔════╝██╔══██╗████╗   ████║██╔══██╗██║██╔╝       ██╔══██╗██╔════╝ ██╗  ██╔╝
 * ██║      ██║  █████╗  █████╗  ███████║██╗██╗ ██╗██║██████╔╝████╔╝ ██████╗██║  ██║█████╗    ██╗██╔╝
 * ██║      ██║  ██╔══╝  ██╔══╝  ██╔══██║██║ ████╔╝██║██╔══██╗██╗██╗ ╚═════╝██║  ██║██╔══╝     ███╔╝
 * ██████╗██████╗██║     ███████╗██║  ██║██║  ██╔╝ ██║██║  ██║██║ ██╗       ██████╔╝███████╗    █╔╝
 * ╚═════╝╚═════╝╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝ ╚═╝       ╚═════╝ ╚══════╝    ╚╝
 */
@Composable
@Preview
fun App() {
    LifeMarkMaterialTheme { // Custom Material Theme.
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