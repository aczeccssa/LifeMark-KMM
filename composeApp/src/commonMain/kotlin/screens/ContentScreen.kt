package screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import cafe.adriel.voyager.core.screen.Screen
import composes.ColorAssets
import data.SpecificConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class RegisterTabScreen {
    HOME_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.Home

        @Composable
        override fun target() {
            HomeView()
        }
    },
    COMMUNITY_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.DateRange

        @Composable
        override fun target() {
            TempView(description, imageVector)
        }
    },
    CHAT_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.Notifications

        @Composable
        override fun target() {
            TempView(description, imageVector)
        }
    },
    PROFILE_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.AccountCircle

        @Composable
        override fun target() {
            TempView(description, imageVector)
        }
    };

    // Properties
    val description: String = this.name.replace("_", " ").lowercase()

    // Abstract properties
    abstract val imageVector: ImageVector

    // Abstract method
    @Composable
    abstract fun target()
}

object ContentScreen : Screen {
    private val navigationHeaderContainerRounded = 32.dp
    private val navigationHeaderContainerPadding = 18.dp
    private val headerAvatarSize = 42.dp
    private val headerMainContainerVerticalPadding = 6.dp
    private val headerInnerContentPadding = 10.dp
    private val headerMessageContainerRounded =
        navigationHeaderContainerRounded - navigationHeaderContainerPadding
    private val headerMessageContainerIconSize = 22.dp
    private val navigationIconSize = 28.dp
    private val navigationIconPaddingTop = 4.dp
    private val navigationIconPaddingBottom = 2 * navigationIconPaddingTop

    @Composable
    override fun Content() {
        // Properties
        // Current tab screen signal(MutableState)
        val currentTab = remember { mutableStateOf(RegisterTabScreen.HOME_SCREEN) }

        // Main container
        Surface {
            /*
            * To fit lost space for compose? using Modifier.fillMaxWidth().weight(1f) or MaxHeight is the same.
            */

            Scaffold(
                // Top header bar
                topBar = { HeaderBar() },
                // Center content place
                content = {
                    val systemNavigationBarHeight =
                        SpecificConfiguration.edgeSafeArea.asPaddingValues()
                            .calculateBottomPadding().value.dp
                    val navigationBarHeight =
                        navigationHeaderContainerPadding + navigationIconPaddingTop + navigationIconPaddingBottom + navigationIconSize

                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .background(MaterialTheme.colors.background)
                            .padding(bottom = systemNavigationBarHeight + navigationBarHeight)
                    ) { currentTab.value.target() }
                },
                // Bottom navigation bar
                bottomBar = { NavigationBar(currentTab) })
        }
    }

    @Composable
    private fun HeaderBar() {
        var showHeadMessage by remember { mutableStateOf(true) }
        val containerClipShape = // Below corner shape.
            RoundedCornerShape(
                0.dp, 0.dp, navigationHeaderContainerRounded, navigationHeaderContainerRounded
            )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.shadow(
                elevation = 12.dp,
                spotColor = ColorAssets.SurfaceShadow.value,
                shape = containerClipShape
            ).fillMaxWidth().clip(containerClipShape).background(MaterialTheme.colors.surface)
                .statusBarsPadding().padding(horizontal = navigationHeaderContainerPadding)
                .padding(bottom = navigationHeaderContainerPadding)
        ) {
            // Main header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = headerMainContainerVerticalPadding)
            ) {
                // Title
                Text(
                    "MyDash",
                    color = MaterialTheme.colors.primary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.width(headerInnerContentPadding))

                // Avatar
                Box(
                    Modifier.size(headerAvatarSize).clip(CircleShape)
                        .background(MaterialTheme.colors.error)
                ) { }
            }

            // Animated flip message container
            AnimatedVisibility(showHeadMessage) {
                // Container
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(top = headerInnerContentPadding).fillMaxWidth()
                        .clip(RoundedCornerShape(headerMessageContainerRounded))
                        .background(MaterialTheme.colors.secondary)
                        .padding(headerInnerContentPadding)
                ) {
                    // Message title && Hidden button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Title
                        Text("Title", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)

                        // Close button
                        Icon(imageVector = Icons.Rounded.Close, contentDescription = null,
                            // tint = MaterialTheme.colors.onSecondary,
                            modifier = Modifier.size(headerMessageContainerIconSize)
                                .clickable { showHeadMessage = false })
                    }

                    // Message contents
                    Text("Content of message...", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }

    @Composable
    private fun NavigationBar(currentState: MutableState<RegisterTabScreen>) {
        val containerClipShape = // Behave rounder shape.
            RoundedCornerShape(
                navigationHeaderContainerRounded, navigationHeaderContainerRounded, 0.dp, 0.dp
            )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.shadow(
                elevation = 12.dp,
                spotColor = ColorAssets.SurfaceShadow.value,
                shape = containerClipShape
            ).fillMaxWidth().clip(containerClipShape).background(MaterialTheme.colors.surface)
                .navigationBarsPadding().padding(horizontal = navigationHeaderContainerPadding)
                .padding(top = navigationHeaderContainerPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = navigationHeaderContainerPadding)
            ) {
                RegisterTabScreen.entries.forEach {
                    Icon(
                        imageVector = it.imageVector,
                        contentDescription = it.description,
                        tint = if (it === currentState.value) MaterialTheme.colors.primary else Color.Gray,
                        modifier = Modifier.clickable(
                            onClick = { currentState.value = it },
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

@Composable
@Preview
private fun CONTENT_SCREEN_PREVIEW() {
    ContentScreen.Content()
}