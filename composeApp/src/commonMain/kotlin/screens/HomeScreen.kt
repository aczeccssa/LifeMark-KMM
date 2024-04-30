package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import composes.LargeButton
import composes.Notifiction
import data.models.ResponseData
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

object MainApplicationNavigator : Screen {
    @Composable
    override fun Content() {
        Navigator(ContentScreen) { navigator -> // HomeScreen
            SlideTransition(navigator)
        }
    }
}

@Composable
@Preview
fun HomeView() {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        try {
            val client = HttpClient()
            val response = client.get("http://120.79.35.203:3000")
            val result = Json.decodeFromString<ResponseData<String>>(response.bodyAsText())
            println(result.toString())
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                .padding(12.dp)
        ) {
            LargeButton(
                text = "Experimental Functions", modifier = Modifier.fillMaxWidth()
            ) {
                navigator.push(ExperimentalFunListScreen)
            }

            Spacer(Modifier.height(24.dp))

            Notifiction("Example", "This is an example message for test!")

            Spacer(Modifier.height(12.dp))

            Notifiction("Example with avatra", "This is an example message with an avatra for test") {
                KamelImage(
                    resource = asyncPainterResource(data = Url("https://cdn.seovx.com/?mom=302")),
                    contentDescription = "Ramdom image test header",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().weight(1f).clip(CircleShape)
                        .background(MaterialTheme.colors.error)
                )
            }
        }
    }
}