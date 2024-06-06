package screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import components.ColorAssets
import components.RegisterTabScreen
import components.notifications.NotificationQueue
import components.properties
import components.snapalert.SnapAlertQueue
import viewmodel.SnapAlertViewModel

object MainApplicationNavigator : Screen {
    @Composable
    override fun Content() {
        // MARK: Main contents
        Navigator(ContentScreen) { navigator -> // HomeScreen
            LaunchedEffect(navigator.size) {
                SnapAlertViewModel.updateScreenState(navigator.lastItem == ContentScreen)
                println("Items: ${navigator.items}")
            }
            SlideTransition(navigator)
        }

        // MARK: Snap alert queue
        SnapAlertQueue()

        // MARK: Notification queue
        NotificationQueue()
    }
}

// Configurations
// Privates.
private val MAIN_CONTAINER_PADDING = 18.dp
private val NAVIGATION_ICON_SIZE = 30.dp
private val NAVIGATION_ICON_PADDING_BOTTOM = 12.dp
private val NAVIGATION_HEADER_CONTAINER_ROUNDED = 28.dp
private const val NAVIGATION_ICON_DEFAULT_SCALE = 1.1f
private const val NAVIGATION_ICON_SELECTED_SCALE = 1.2f

// Public bar height value.
val NAVIGATION_BAR_HEIGHT get() = MAIN_CONTAINER_PADDING + NAVIGATION_ICON_PADDING_BOTTOM + NAVIGATION_ICON_SIZE

// MARK: As same as the `ContentView` of SwiftUI! 😊
object ContentScreen : Screen {
    // Main container
    @Composable
    override fun Content() {
        val currentTab by remember { RegisterTabScreen.contentScreenPrinter }

        // Enable surface in this screen.
        Surface {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
            ) {
                // Content area
                Box(Modifier.fillMaxSize().weight(1f).background(MaterialTheme.colors.background)) {
                    // Container main content
                    currentTab.target()
                }

                // Navigation bar
                NavigationBar()
            }
        }
    }

    @Composable
    private fun NavigationBar() {
        /** Behave corner shape */
        val containerClipShape = RoundedCornerShape(
            NAVIGATION_HEADER_CONTAINER_ROUNDED, NAVIGATION_HEADER_CONTAINER_ROUNDED, 0.dp, 0.dp
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.zIndex(3f).shadow(
                elevation = 12.dp,
                spotColor = ColorAssets.SurfaceShadow.value,
                shape = containerClipShape
            ).fillMaxWidth().clip(containerClipShape).background(MaterialTheme.colors.surface)
                .navigationBarsPadding().padding(horizontal = MAIN_CONTAINER_PADDING * 2)
                .padding(top = MAIN_CONTAINER_PADDING)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = MAIN_CONTAINER_PADDING)
            ) {
                RegisterTabScreen.entries.forEach {
                    NavigatorIcon(it)
                }
            }
        }
    }

    @Composable
    private fun NavigatorIcon(self: RegisterTabScreen) {
//        val iconAlpha = animateFloatAsState(
//            targetValue = if (it === RegisterTabScreen.contentScreenPrinter.value) 1f else 0.2f,
//            animationSpec = tween(durationMillis = MaterialTheme.properties.defaultAnimationDuration.toInt() / 2)
//        )
//        val iconScale = animateFloatAsState(
//            targetValue = if (it === RegisterTabScreen.contentScreenPrinter.value) 1f else 0.8f,
//            animationSpec = tween(durationMillis = MaterialTheme.properties.defaultAnimationDuration.toInt() / 2)
//        )
        val outlineAlpha = animateFloatAsState(
            targetValue = if (self === RegisterTabScreen.contentScreenPrinter.value) 0f else 1f,
            animationSpec = tween(durationMillis = MaterialTheme.properties.defaultAnimationDuration.toInt() / 2)
        )
        val filledAlpha = animateFloatAsState(
            targetValue = if (self === RegisterTabScreen.contentScreenPrinter.value) 1f else 0f,
            animationSpec = tween(durationMillis = MaterialTheme.properties.defaultAnimationDuration.toInt() / 2)
        )
        val filledScale = animateFloatAsState(
            targetValue = if (self === RegisterTabScreen.contentScreenPrinter.value) NAVIGATION_ICON_SELECTED_SCALE else NAVIGATION_ICON_DEFAULT_SCALE,
            animationSpec = tween(durationMillis = MaterialTheme.properties.defaultAnimationDuration.toInt() / 2)
        )

        Box(
            modifier = Modifier.clickable(
                onClick = { RegisterTabScreen.setContentScreenPrinter(self) },
                indication = null,
                interactionSource = MutableInteractionSource()
            ).padding(bottom = NAVIGATION_ICON_PADDING_BOTTOM)
                .size(NAVIGATION_ICON_SIZE), // .scale(iconScale.value),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = self.imageVector.filled,
                contentDescription = self.description,
                tint = ColorAssets.LMPurple.value,
                modifier = Modifier.scale(filledScale.value).alpha(filledAlpha.value)
            )

            Icon(
                imageVector = self.imageVector.outline,
                contentDescription = self.description,
                tint = ColorAssets.LMPrimaryUnselected.value,
                modifier = Modifier.scale(NAVIGATION_ICON_DEFAULT_SCALE).alpha(outlineAlpha.value)
            )
        }
    }
}