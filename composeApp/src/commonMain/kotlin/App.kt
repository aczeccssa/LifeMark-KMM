
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import components.LifeMarkMaterialTheme
import components.notifications.NotificationQueue
import components.snapalert.SnapAlertQueue
import data.modules.initKoin
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.register.SignatureScreen
import viewmodel.SnapAlertViewModel

// ██╗    ██████╗███████╗███████╗ █████╗ ███╗     ███╗██████╗ ██╗ ██╗       ██████╗ ███████╗██╗    ██╗
// ██║    ╚═██╔═╝██╔════╝██╔════╝██╔══██╗████╗   ████║██╔══██╗██║██╔╝       ██╔══██╗██╔════╝ ██╗  ██╔╝
// ██║      ██║  █████╗  █████╗  ███████║██╗██╗ ██╗██║██████╔╝████╔╝ ██████╗██║  ██║█████╗    ██╗██╔╝
// ██║      ██║  ██╔══╝  ██╔══╝  ██╔══██║██║ ████╔╝██║██╔══██╗██╗██╗ ╚═════╝██║  ██║██╔══╝     ███╔╝
// ██████╗██████╗██║     ███████╗██║  ██║██║  ██╔╝ ██║██║  ██║██║ ██╗       ██████╔╝███████╗    █╔╝
// ╚═════╝╚═════╝╚═╝     ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝ ╚═╝       ╚═════╝ ╚══════╝    ╚╝
/**
 * TODO: Feature list
 *   1. Player Birthday Event Congratulation~~!
 *   2. ...
 *
 * MARK: Transition Animation.
 *     1。 `ScaleTransition`: Scale size and color opacity transition.
 *     2. `FadeTransition`: Only opacity transition.
 *     3. `SlideTransition`: Slide start to end transition.
 * MARK: Custom
 *     4. Can enable custom transition in -> `https://voyager.adriel.cafe/transitions`.
 */
@Composable
@Preview
fun App() {
    // Init koin
    initKoin()

    // App
    LifeMarkMaterialTheme { // Custom Material Theme.
        Navigator(SignatureScreen) { navigator ->
            SnapAlertViewModel.updateScreenState(false)
            ScaleTransition(navigator, animationSpec = spring(stiffness = Spring.StiffnessLow))
        }

        // MARK: Snap alert queue
        SnapAlertQueue()

        // MARK: Notification queue
        NotificationQueue()
    }
}