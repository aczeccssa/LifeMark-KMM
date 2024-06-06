package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.ColorAssets
import components.ListItem
import components.Rectangle
import components.navigator.MainNavigator
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ColorPicker
import compose.icons.evaicons.outline.Info
import data.SpecificConfiguration
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import screens.experimental.ExperimentalImagePickerScreen
import viewmodel.ProfileScreenViewModel

@Composable
fun ProfileScreen(viewModel: ProfileScreenViewModel = viewModel { ProfileScreenViewModel() }) {
    val accountAvatar by remember { viewModel.accountAvatar }
    val account = viewModel.account
    val scrollState = rememberScrollState()
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        // MARK: Do not call this method on viewmodel init block.
        viewModel.updateAccountAvatar()
    }

    // Components
    Column {
        MainNavigator("Settings")

        Row(
            modifier = Modifier.clickable { }.fillMaxWidth().padding(vertical = 12.dp)
                .padding(horizontal = SpecificConfiguration.defaultContentPadding),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Rectangle(
                size = DpSize(52.dp, 52.dp),
                modifier = Modifier.clip(CircleShape).background(MaterialTheme.colors.secondary)
            ) {
                accountAvatar?.let {
                    KamelImage(
                        resource = asyncPainterResource(it),
                        contentDescription = account?.userCredentials?.username,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Text(
                text = account?.userCredentials?.username ?: "Click to login",
                style = MaterialTheme.typography.subtitle1
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = ColorAssets.Gray.value
            )
        }

        Divider(Modifier.padding(SpecificConfiguration.defaultContentPadding, 0.dp))

        Column(
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth()
                .background(MaterialTheme.colors.background).padding(0.dp, 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            ListItem(EvaIcons.Outline.Info/*FontAwesomeIcons.Solid.Info*/, ColorAssets.SK.FillBlue.value, "About Lifemark 2024") {
                navigator.push(InfoScreen)
            }

            ListItem(EvaIcons.Outline.ColorPicker/*FontAwesomeIcons.Solid.PhotoVideo*/, ColorAssets.SK.FillOrange.value, "Experimental Picker")  {
                navigator.push(ExperimentalImagePickerScreen)
            }
        }
    }
}