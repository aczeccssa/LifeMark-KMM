package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import composes.LargeButton
import data.models.ResponseData
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                .padding(12.dp)
        ) {
            LargeButton(
                text = "Experimental Functions", modifier = Modifier.fillMaxWidth()
            ) {
                navigator.push(ExperimentalFunListScreen)
            }
        }
    }
}