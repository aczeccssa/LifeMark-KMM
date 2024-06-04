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
 *
 * TODO: Feature list
 *     1. Player Birthday Event Congratulation~~!
 *     2. ...
 */
@Composable
@Preview
fun App() {
    LifeMarkMaterialTheme { // Custom Material Theme.
        Navigator(SplashScreen) { navigator ->
            /**
             * MARK: Transition Animation.
             *     `ScaleTransition`: Scale size and color opacity transition.
             *     `FadeTransition`: Only opacity transition.
             *     `SlideTransition`: Slide start to end transition.
             * MARK: Custom
             *     Can enable custom transition in -> `https://voyager.adriel.cafe/transitions`.
             */
            ScaleTransition(navigator, animationSpec = spring(stiffness = Spring.StiffnessLow))
        }
    }
}