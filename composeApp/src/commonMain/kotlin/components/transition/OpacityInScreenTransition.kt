package components.transition

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.stack.StackEvent.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScreenTransition
import cafe.adriel.voyager.transitions.ScreenTransitionContent

private object OpacityInScreenTransition : CustomScreenTransition {
    val animationSpec = spring<Float>(stiffness = Spring.StiffnessMedium)
    override fun screenTransition(
        scope: AnimatedContentTransitionScope<Screen>,
        navigator: Navigator,
    ): ContentTransform =
        when (navigator.lastEvent) {
            Pop -> fadeIn(animationSpec) togetherWith fadeOut(animationSpec)
            else -> scaleIn(animationSpec) togetherWith scaleOut(animationSpec)
        }
}

@Composable
fun OpacityInScreenTransition(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    defaultTransition: CustomScreenTransition = OpacityInScreenTransition,
    content: ScreenTransitionContent
) {
    ScreenTransition(
        navigator = navigator,
        modifier = modifier,
        content = content,
        transition = {
            val transitionSource = when (navigator.lastEvent) {
                Pop, Replace -> initialState
                Idle, Push -> initialState
            }

            // Get the transition from screen, if it is prociding any
            val screenTransition = (transitionSource as? CustomScreenTransition)
                ?.screenTransition(this, navigator)
                ?: defaultTransition.screenTransition(this, navigator)

            // Set the zIndex for the transition:
            // -> screens heigher up on the stack must rendered on top of screens below
            // during transitions, this is important,
            // We use the index of the item to determine the zIndex in the UI
            val stackSize = navigator.items.size
            screenTransition.targetContentZIndex = when (navigator.lastEvent) {
                // Make sure the content this;s popped is rendered on top
                Pop, Replace -> (stackSize - 1)
                // Make sure that content that's pushed is rendered on top
                Idle, Push -> (stackSize)
            }.toFloat()

            // Return the transition
            screenTransition
        }
    )
}