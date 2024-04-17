package screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import composes.NavigationHeader
import composes.SnapAlert
import data.LocalScreenConfiguration
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.*
import org.jetbrains.compose.ui.tooling.preview.Preview

object InfoScreen : Screen {
    @Composable
    @Preview
    override fun Content() {
        val isSnackBarVisiable = remember { mutableStateOf(false) }
        val snackBarOffsetAnimate = animateDpAsState(
            targetValue = if (isSnackBarVisiable.value) 0.dp else 100.dp,
            animationSpec = tween(durationMillis = 400),
            label = "SnackBar offset transition"
        )

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
                modifier = Modifier.fillMaxSize().safeContentPadding()
            ) {
                SnapAlert(
                    message = "LifeMark 2024 Dev version 0.1.0",
                    modifier = Modifier.offset(y = snackBarOffsetAnimate.value)
                ) {
                    Button(onClick = { isSnackBarVisiable.value = false }) { Text("Res") }
                }
            }
        }
    }

    @Composable
    private fun KMMInfo() {
        val navigator = LocalNavigator.currentOrThrow
        val screenSize = LocalScreenConfiguration()

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val kotlinSqureCover = "https://s21.ax1x.com/2024/04/16/pFxihRg.png"
            KamelImage(
                resource = asyncPainterResource(data = Url(kotlinSqureCover)),
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
                text = "Resolution: ${screenSize.height} x ${screenSize.width}",
                fontStyle = FontStyle.Italic,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}