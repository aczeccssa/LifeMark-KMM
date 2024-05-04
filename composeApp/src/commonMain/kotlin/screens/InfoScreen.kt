package screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import components.NavigationHeader
import components.SnapAlert
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

object InfoScreen : Screen {
    @Composable
    override fun Content() {
        val isSnackBarVisiable = remember { mutableStateOf(false) }
        val snackBarOffsetAnimate = animateDpAsState(
            targetValue = if (isSnackBarVisiable.value) 0.dp else 100.dp,
            animationSpec = tween(durationMillis = 400),
            label = "SnackBar offset transition"
        )
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.headerHeight + 28.dp

        // On appear show snack bar
        LaunchedEffect(Unit) {
            isSnackBarVisiable.value = true
        }

        // Compose
        Surface {
            NavigationHeader("About LifeMark 2024")

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
            ) {
                KMMInfo()
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().safeContentPadding().padding(top = topOffset)
            ) {
                SnapAlert(
                    message = "LifeMark 2024 Dev version 0.1.0",
                    modifier = Modifier.navigationBarsPadding().offset(y = snackBarOffsetAnimate.value)
                ) {
                    Button(onClick = { isSnackBarVisiable.value = false }) { Text("Res") }
                }
            }
        }
    }

    @Composable
    private fun KMMInfo() {
        val screenSize = SpecificConfiguration.localScreenConfiguration

        val pixelResText = "Resolution: ${screenSize.nativeBounds.height}px x ${screenSize.nativeBounds.width}px"
        val renderResText =
            "Render: ${screenSize.bounds.height.value.toInt()}dp x ${screenSize.bounds.width.value.toInt()}dp"

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val kotlinSquareCover = "kotlinSquareCover"
            KamelImage(
                resource = asyncPainterResource(data = Url(kotlinSquareCover)),
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

            Text(
                text = pixelResText, fontStyle = FontStyle.Italic, fontSize = 13.sp, fontWeight = FontWeight.Medium
            )

            Text(
                text = renderResText, fontStyle = FontStyle.Italic, fontSize = 13.sp, fontWeight = FontWeight.Medium
            )
        }
    }
}