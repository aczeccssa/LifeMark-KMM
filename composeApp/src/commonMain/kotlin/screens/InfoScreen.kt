package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import components.ColorAssets
import components.NavigationHeader
import components.SurfaceColors
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.kotlin_full_color_logo_mush_rgb
import org.jetbrains.compose.resources.painterResource
import viewmodel.SnapAlertViewModel

object InfoScreen : Screen {
    @Composable
    override fun Content() {
        var isSnackBarVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            isSnackBarVisible = true
            SnapAlertViewModel.pushSnapAlert("LifeMark 2024 Dev version 0.1.0")
        }

        // Compose
        Surface {
            NavigationHeader(
                title = "About LifeMark 2024",
                configuration = NavigationHeaderConfiguration.defaultConfiguration.copy(
                    color = SurfaceColors.defaultNavigatorColors.copy(
                        surface = ColorAssets.Background
                    )
                )
            )

            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                    .padding(bottom = NAVIGATION_BAR_HEIGHT),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { KMMInfo() }
        }
    }

    @Composable
    private fun KMMInfo() {
        val screenSize = SpecificConfiguration.localScreenConfiguration

        val textStyle = TextStyle(
            fontStyle = FontStyle.Italic, fontSize = 13.sp, fontWeight = FontWeight.Medium
        )

        val pixelResText =
            "Resolution: ${screenSize.nativeBounds.height}px x ${screenSize.nativeBounds.width}px"
        val renderResText =
            "Render: ${screenSize.bounds.height.value.toInt()}dp x ${screenSize.bounds.width.value.toInt()}dp"

        Column(Modifier, Arrangement.Center, Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(Res.drawable.kotlin_full_color_logo_mush_rgb),
                contentDescription = "org.kotlinlang.chunks.hero-cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(240.dp)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "LIFEMARK-2024",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.primary
            )

            Text(text = pixelResText, style = textStyle)

            Text(text = renderResText, style = textStyle)
        }
    }
}