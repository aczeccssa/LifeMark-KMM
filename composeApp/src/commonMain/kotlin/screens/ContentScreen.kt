package screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import components.ColorAssets
import components.RegisterTabScreen
import components.notifications.NotificationQueue
import components.snapalert.SnapAlertQueue

object MainApplicationNavigator : Screen {
    @Composable
    override fun Content() {
        Navigator(ContentScreen) { navigator -> // HomeScreen
            SlideTransition(navigator)
        }

        // MARK: Snap alert queue
        SnapAlertQueue()

        // MARK: Notification queue
        NotificationQueue()
    }
}

// Configurations
val mainContainerPadding = 18.dp
val navigationIconSize = 28.dp
val navigationIconPaddingTop = 4.dp
val navigationIconPaddingBottom = 2 * navigationIconPaddingTop
private val navigationHeaderContainerRounded = 28.dp

// As same as the `ContentView` of SwiftUI! 😊
private object ContentScreen : Screen {
    // Main container
    @Composable
    override fun Content() {
        val currentTab by remember { RegisterTabScreen.contentScreenPrinter }

        // Enable surface in this screen.
        Surface {
            // To fit lost space for compose? using Modifier.fillMaxWidth().weight(1f) or MaxHeight is the same.
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
            navigationHeaderContainerRounded, navigationHeaderContainerRounded, 0.dp, 0.dp
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.zIndex(3f).shadow(
                elevation = 12.dp,
                spotColor = ColorAssets.SurfaceShadow.value,
                shape = containerClipShape
            ).fillMaxWidth().clip(containerClipShape).background(MaterialTheme.colors.surface)
                .navigationBarsPadding().padding(horizontal = mainContainerPadding * 2)
                .padding(top = mainContainerPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = mainContainerPadding)
            ) {
                RegisterTabScreen.entries.forEach {
                    Icon(
                        imageVector = it.imageVector,
                        contentDescription = it.description,
                        tint = if (it === RegisterTabScreen.contentScreenPrinter.value)
                            MaterialTheme.colors.primary else Color.Gray,
                        modifier = Modifier.clickable(
                            onClick = { RegisterTabScreen.setContentScreenPrinter(it) },
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ).padding(
                            top = navigationIconPaddingTop, bottom = navigationIconPaddingBottom
                        ).size(navigationIconSize)
                    )
                }
            }
        }
    }
}