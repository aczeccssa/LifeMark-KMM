package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
        Navigator(HomeScreen) { navigator ->
            SlideTransition(navigator)
        }
    }
}

object HomeScreen : Screen {
    @Composable
    @Preview
    override fun Content() {
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
                modifier = Modifier
                    .fillMaxSize().background(MaterialTheme.colors.background)
                    .navigationBarsPadding()
            ) {
                NavigatorHeaderBar()

                LargeButton("Experimental Functions", modifier = Modifier.safeContentPadding().fillMaxWidth()) {
                    navigator.push(ExperimentalFunListScreen)
                }
            }
        }
    }

    @Composable
    private fun NavigatorHeaderBar() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = "navigator_bar_header_icon",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(34.dp)
                )

                Spacer(Modifier.width(6.dp))

                Text("Home", fontWeight = FontWeight.SemiBold, fontSize = 24.sp)
            }

            Spacer(Modifier.width(0.dp))
        }
    }
}

@Composable
@Preview
fun App_Previewer() {
    HomeScreen
}