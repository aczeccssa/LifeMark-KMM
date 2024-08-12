
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import components.LifeMarkMaterialTheme
import components.notifications.NotificationQueue
import components.snapalert.SnapAlertQueue
import data.modules.initKoin
import data.platform.LocalPreferences
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.merge.MuseumPaging
import screens.merge.Sp
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

        val isLoggedIn = LocalPreferences.getBoolean(Sp.USERNAME.toString(), true)

        println("APP -> isLoggedIn: $isLoggedIn")

        if(isLoggedIn) {

            Navigator(MuseumPaging()) { navigator ->
                SnapAlertViewModel.updateScreenState(false)
                ScaleTransition(navigator, animationSpec = spring(stiffness = Spring.StiffnessLow))
            }
        } else {
            Navigator(SignatureScreen) { navigator ->
                SnapAlertViewModel.updateScreenState(false)
                ScaleTransition(navigator, animationSpec = spring(stiffness = Spring.StiffnessLow))
            }
        }



        // MARK: Snap alert queue
        SnapAlertQueue()

        // MARK: Notification queue
        NotificationQueue()
    }
}