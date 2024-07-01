package screens.mainscreens

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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.ColorAssets
import components.ListItem
import components.Rectangle
import components.navigator.MainNavigator
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.ColorPicker
import compose.icons.evaicons.fill.Info
import compose.icons.evaicons.outline.Activity
import compose.icons.evaicons.outline.Grid
import compose.icons.evaicons.outline.List
import compose.icons.evaicons.outline.Monitor
import data.SpecificConfiguration
import data.modules.getViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import screens.NAVIGATION_BAR_HEIGHT
import screens.experimental.ExperimentalComponentsScreen
import screens.experimental.ExperimentalGlobalSheepTestScreen
import screens.experimental.ExperimentalHazeMaterialScreen
import screens.experimental.ExperimentalImagePickerScreen
import screens.profiles.InfoScreen
import viewmodel.ProfileScreenViewModel

@Composable
fun ProfileScreen(
    // viewModel: ProfileScreenViewModel = viewModel { ProfileScreenViewModel() }
) {
    val viewModel: ProfileScreenViewModel = getViewModel()
    val scrollState = rememberScrollState()
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        // MARK: Do not call this method on viewmodel init block.
        viewModel.updateAccountAvatar()
    }

    // Components
    Column(Modifier.fillMaxSize()) {
        MainNavigator("Settings")

        ProfileHeader(viewModel)

        Divider(Modifier.padding(SpecificConfiguration.defaultContentPadding, 0.dp))

        Column(
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth()
                .background(MaterialTheme.colors.background).padding(0.dp, 12.dp).padding(bottom = NAVIGATION_BAR_HEIGHT),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            ListItem(
                imageVector = EvaIcons.Fill.Info,
                tint = ColorAssets.SK.FillBlue.value,
                title = "About Lifemark 2024"
            ) { navigator.push(InfoScreen) }

            ListItem(
                imageVector = EvaIcons.Fill.ColorPicker,
                tint = ColorAssets.SK.FillOrange.value,
                title = "Experimental Picker"
            ) { navigator.push(ExperimentalImagePickerScreen) }

            ListItem(
                imageVector = EvaIcons.Outline.Monitor,
                tint = ColorAssets.SK.FillBlue.value,
                title = "Specific Platform",
                sub = "Experimental functions for specific platforms"
            ) { navigator.push(ExperimentalComponentsScreen) }

            ListItem(
                imageVector = EvaIcons.Outline.List,
                tint = ColorAssets.SK.FillOrange.value,
                title = "Global Sheep",
                sub = "Experimental global sheep feature."
            ) { navigator.push(ExperimentalGlobalSheepTestScreen) }

            ListItem(
                imageVector = EvaIcons.Outline.Grid,
                tint = ColorAssets.SK.FillYellow.value,
                title = "Haze Material",
                sub = "Experimental haze blur effect feature."
            ) { navigator.push(ExperimentalHazeMaterialScreen) }
        }
    }
}

@Composable
private fun ProfileHeader(viewModel: ProfileScreenViewModel) {
    val accountAvatar by remember { viewModel.accountAvatar }
    val account = viewModel.account

    Row(
        modifier = Modifier.clickable { }.fillMaxWidth().padding(vertical = 12.dp)
            .padding(horizontal = SpecificConfiguration.defaultContentPadding),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Rectangle(
            size = DpSize(60.dp, 60.dp),
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

        Icon(EvaIcons.Outline.Activity, null, Modifier, ColorAssets.Gray.value)
    }
}