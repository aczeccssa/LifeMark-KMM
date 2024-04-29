package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import composes.LargeButton
import data.models.ResponseData
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
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
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.error)
        ) {
            LargeButton("Experimental Functions", modifier = Modifier.safeContentPadding().fillMaxWidth()) {
                navigator.push(ExperimentalFunListScreen)
            }
        }
    }
}