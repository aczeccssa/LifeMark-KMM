package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import composes.NavigationView
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.jetbrains.compose.ui.tooling.preview.Preview

object HomeScreen : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    @Preview
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenSize = LocalWindowInfo.current.containerSize

        LaunchedEffect(Unit) {
            val client = HttpClient()
            val response = client.get("http://120.79.35.203:3000")
            println(response.bodyAsText())
        }

        Surface {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
            ) {
                Spacer(Modifier.fillMaxHeight(0.2f))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable(
                        onClick = {
                            navigator.push(InfoScreen) // Navigate to new screen.
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                ) {
                    val kotlinSqureCover = "https://s21.ax1x.com/2024/04/16/pFxihRg.png"
                    KamelImage(
                        resource = asyncPainterResource(data = Url(kotlinSqureCover)),
                        contentDescription = "org.kotlinlang.chunks.hero-cover",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(240.dp)
                    )
                    // Symbols R: painterResource(Res.drawable.kotlin_logo_fullcolor)

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

                Spacer(Modifier.fillMaxHeight(0.4f))

                Button({
                    navigator.push(NavigationView("Color preview") { ColorPreviewer() })
                }) { Text("Material Color Preview") }
            }
        }
    }
}


@Composable
@Preview
fun App_Previewer() {
    HomeScreen
}