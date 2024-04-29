package screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.zIndex
import cafe.adriel.voyager.core.screen.Screen
import data.SpecificConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class RegistedTabScreen {
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

            TempView(this.name.lowercase(), imageVector)

        }
    },
    CHAT_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.Notifications

        @Composable
        override fun target() {

            TempView(this.name.lowercase(), imageVector)

        }
    },
    PROFILE_SCREEN {
        override val imageVector: ImageVector = Icons.Rounded.AccountCircle

        @Composable
        override fun target() {

            TempView(this.name.lowercase(), imageVector)

        }
    };

    abstract val imageVector: ImageVector
    val desctiprion: String = this.name.replace("_", " ").lowercase()


    @Composable
    abstract fun target()
}

object ContentScreen : Screen {
    private val navigationHeaderContainerRounded = 32.dp
    private val navigationHeaderContainerPadding = 18.dp
    private val headerAvatarSize = 42.dp
    private val headerMainConatinerVerticalPadding = 6.dp
    private val headerInnerContentPadding = 10.dp
    private val headerMessageContainerRounded = navigationHeaderContainerRounded - navigationHeaderContainerPadding
    private val headerMessageContainerIconSize = 22.dp
    private val navigationIconSize = 28.dp

    @Composable
    override fun Content() {
        // Properties
        // Current tab screen signal(MutableState)
        val currentTab = remember { mutableStateOf(RegistedTabScreen.HOME_SCREEN) }

        // Main container
        Surface {
            // Navigator
            Column(Modifier.fillMaxSize()) {
                // Header bar
                // ZLevel: 2
                HeaderBar()

                // Set background color to MaterialTheme.colors.background
                // Tab Frame
                // Mutable view change
                currentTab.value.target()


                // Navigation bar
                // ZLevel: 2
                NavigationBar(currentTab)
            }
        }
    }

    @Composable
    private fun HeaderBar() {
        var showHeadMessage by remember { mutableStateOf(true) }
        val containerClipShape = // Below corner shape.
            RoundedCornerShape(0.dp, 0.dp, navigationHeaderContainerRounded, navigationHeaderContainerRounded)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .shadow(elevation = 12.dp, spotColor = Color(0x10333333), shape = containerClipShape)
                .fillMaxWidth()
                .clip(containerClipShape)
                .background(MaterialTheme.colors.surface)
                .statusBarsPadding()
                .padding(horizontal = navigationHeaderContainerPadding)
                .padding(bottom = navigationHeaderContainerPadding)
        ) {
            // Main header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(vertical = headerMainConatinerVerticalPadding)
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
                Box(Modifier.size(headerAvatarSize).clip(CircleShape).background(MaterialTheme.colors.error)) { }
            }

            // Animated flip message container
            AnimatedVisibility(showHeadMessage) {
                // Container
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(top = headerInnerContentPadding)
                        .fillMaxWidth()
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
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            // tint = MaterialTheme.colors.onSecondary,
                            modifier = Modifier.size(headerMessageContainerIconSize)
                                .clickable { showHeadMessage = false }
                        )
                    }

                    // Message contents
                    Text("Content of message...", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }

    @Composable
    private fun NavigationBar(currentState: MutableState<RegistedTabScreen>) {
        val navigationIconPaddingTop = 4.dp
        val navigationIconPaddingBottom = 2 * navigationIconPaddingTop
        val containerClipShape = // Behive rounder shape.
            RoundedCornerShape(navigationHeaderContainerRounded, navigationHeaderContainerRounded, 0.dp, 0.dp)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .shadow(elevation = 12.dp, spotColor = Color(0x10333333), shape = containerClipShape)
                .fillMaxWidth()
                .clip(containerClipShape)
                .background(MaterialTheme.colors.surface)
                .navigationBarsPadding()
                .padding(horizontal = navigationHeaderContainerPadding)
                .padding(top = navigationHeaderContainerPadding)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = navigationHeaderContainerPadding)
            ) {
                RegistedTabScreen.entries.forEach {
                    Icon(
                        imageVector = it.imageVector,
                        contentDescription = it.desctiprion,
                        tint = if (it === currentState.value) MaterialTheme.colors.primary else Color.DarkGray,
                        modifier = Modifier
                            .clickable(onClick = { currentState.value = it })
                            .padding(top = navigationIconPaddingTop, bottom = navigationIconPaddingBottom)
                            .size(navigationIconSize)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun CONTENTSCREEN_PREVIEW() {
    ContentScreen.Content()
}