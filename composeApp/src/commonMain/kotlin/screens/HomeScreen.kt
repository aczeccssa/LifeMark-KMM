package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.LargeButton
import components.notifications.MutableNotification
import components.notifications.Notification
import data.models.ResponseData
import data.network.Apis
import data.platform.generateUUID
import data.resources.generateNotificationData
import data.units.now
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Url
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.poppins_medium_italic
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import viewmodel.NotificationViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun HomeView(viewModel: HomeScreenViewModel = viewModel { HomeScreenViewModel() }) {
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        try {
            viewModel.fetchServer()
        } catch (e: Exception) {
            println("${LocalDateTime.now()} - Error: ${e.message}")
            NotificationViewModel.pushExceptionNotification(e) {
                viewModel.fetchServer()
            }
        }
    }

    Surface {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                .padding(18.dp)
        ) {
            LargeButton(
                text = "Experimental Functions", modifier = Modifier.fillMaxWidth()
            ) {
                navigator.push(ExperimentalFunListScreen)
            }

            Spacer(Modifier.height(12.dp))

            Notification("Example", "This is an example message for test!")

            Spacer(Modifier.height(12.dp))

            Notification(
                "Example with avatar", "This is an example message with an avatar for test"
            ) {
                KamelImage(
                    resource = asyncPainterResource(data = Url("https://cdn.seovx.com/?mom=302")),
                    contentDescription = "Random image test header",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().weight(1f).clip(CircleShape)
                        .background(MaterialTheme.colors.error)
                )
            }

            Spacer(Modifier.height(12.dp))

            MutableNotification(generateNotificationData())

            Spacer(Modifier.height(12.dp))

            LargeButton("Set notification", modifier = Modifier.fillMaxWidth()) {
                NotificationViewModel.pushNotification(generateNotificationData())
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Poppins Bold: custom font test",
                fontFamily = Font(Res.font.poppins_medium_italic).toFontFamily()
            )
        }
    }
}

class HomeScreenViewModel(id: String = generateUUID()) : ViewModel() {
    init {
        println("${LocalDateTime.now()} - View model online: ${id}")
    }

    @Throws(Exception::class)
    suspend fun fetchServer() {
        try {
            val client = HttpClient()
            val response = client.get(Apis.HOST_NAME)
            val result = Json.decodeFromString<ResponseData<String>>(response.bodyAsText())
            println(result.toString())
        } catch (e: Exception) {
            println("${LocalDateTime.now()} - Error: ${e.message}")
            throw e
        }
    }
}