package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import components.Rectangle
import components.RegisterTabScreen
import components.navigator.MainNavigator
import data.SpecificConfiguration
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import viewmodel.ProfileScreenViewModel

@Composable
fun ProfileScreen(viewModel: ProfileScreenViewModel = viewModel { ProfileScreenViewModel() }) {
    val accountAvatar by remember { viewModel.accountAvatar }
    val account = viewModel.account

    val scrollState = rememberScrollState()

    // Components
    Column {
        MainNavigator(RegisterTabScreen.PROFILE_SCREEN.imageVector, "Settings")

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.clickable { }.fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Rectangle(
                size = DpSize(64.dp, 64.dp),
                modifier = Modifier.clip(CircleShape).background(MaterialTheme.colors.surface)
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
                style = MaterialTheme.typography.h6
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(SpecificConfiguration.defaultContentPadding)
        ) {

        }
    }
}

