package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import components.ColumnRoundedContainer
import components.ContainerSize
import components.LargeButton
import components.Rectangle
import components.RegisterTabScreen
import components.RoundedContainer
import components.ViewMoreOpacityMusk
import components.navigator.MainNavigator
import data.Zero
import data.models.MutableNotificationData
import data.network.Apis
import data.resources.LifeMarkIntroduction
import data.resources.generateNotificationData
import data.resources.generateSnapAlertData
import data.units.now
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.poppins_medium_italic
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import screens.experimental.SpaceXLauncherHistory
import viewmodel.NotificationViewModel
import viewmodel.SnapAlertViewModel

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: HomeScreenViewModel = viewModel { HomeScreenViewModel() }) {
    val navigator = LocalNavigator.currentOrThrow
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    fun sheetCloseHandle() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) showBottomSheet = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchServer()
    }

    Column {
        MainNavigator(RegisterTabScreen.HOME_SCREEN.imageVector, "Home") {
            Rectangle(
                DpSize(42.dp, 42.dp), Modifier.clickable {
                    showBottomSheet = true
                }.clip(CircleShape).background(Color.Red)
            )
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth()
                .background(MaterialTheme.colors.background).padding(18.dp)
        ) {
            RoundedContainer(ContainerSize(360.dp)) {
                Column {
                    Text("Introduction", style = MaterialTheme.typography.h6)
                    Text(
                        text = LifeMarkIntroduction.INTRODUCTION,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(0.dp, 8.dp)
                    )
                    Text("Key Features", style = MaterialTheme.typography.h6)

                    LifeMarkIntroduction.MAINS.forEach { line ->
                        Spacer(Modifier.height(6.dp))
                        Text(line, style = MaterialTheme.typography.body2)
                    }
                }
                ViewMoreOpacityMusk(MaterialTheme.colors.surface) { navigator.push(AboutLifeMark) }
            }

            Spacer(Modifier.height(24.dp))

            ColumnRoundedContainer {
                LargeButton("Set notification", modifier = Modifier.fillMaxWidth()) {
                    NotificationViewModel.pushNotification(generateNotificationData())
                }

                Spacer(Modifier.height(12.dp))

                LargeButton("Set snap alert", modifier = Modifier.fillMaxWidth()) {
                    SnapAlertViewModel.pushSnapAlert(generateSnapAlertData())
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Poppins Bold: custom font test",
                    fontFamily = Font(Res.font.poppins_medium_italic).toFontFamily()
                )

                Spacer(Modifier.height(12.dp))

                LargeButton("Experimental Functions", modifier = Modifier.fillMaxWidth()) {
                    navigator.push(ExperimentalFunListScreen)
                }
            }
        }
    }

    // Sheet
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colors.background,
            windowInsets = WindowInsets.Zero
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                    .navigationBarsPadding()
            ) {
                SpaceXLauncherHistory()
            }
        }
    }
}

class HomeScreenViewModel(val id: Uuid = uuid4()) : ViewModel() {
    init {
        println("${LocalDateTime.now()} - Home screen view model online: $id")
    }

    override fun onCleared() {
        println("${LocalDateTime.now()} - Home screen view model offline: $id")
        super.onCleared()
    }

    @Throws(Throwable::class)
    suspend fun fetchServer() {
        try {
            val result = Apis.getServerConnection()
            println(result.toString())
            NotificationViewModel.pushNotification(MutableNotificationData(
                "Server",
                result.main
            ) { it() })
        } catch (e: Exception) {
            println("${LocalDateTime.now()} - Error: ${e.message}")
            NotificationViewModel.pushNotification(e)
        }
    }
}