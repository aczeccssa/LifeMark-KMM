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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import components.SurfaceColors
import components.ViewMoreOpacityMusk
import components.navigator.MainNavigator
import components.secondaryButtonColors
import data.SpecificConfiguration
import data.Zero
import data.models.MutableNotificationData
import data.network.Apis
import data.resources.LifeMarkIntroduction
import data.resources.generateNotificationData
import data.resources.generateSnapAlertData
import data.units.now
import kotlinx.datetime.LocalDateTime
import screens.experimental.SpaceXLauncherHistory
import viewmodel.NotificationViewModel
import viewmodel.SnapAlertViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: HomeScreenViewModel = viewModel { HomeScreenViewModel() }) {
    val navigator = LocalNavigator.currentOrThrow
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
//    val scope = rememberCoroutineScope()
//
//    fun sheetCloseHandle() {
//        scope.launch { sheetState.hide() }.invokeOnCompletion {
//            if (!sheetState.isVisible) showBottomSheet = false
//        }
//    }

    LaunchedEffect(Unit) {
        if (!viewModel.isFirstVisit) viewModel.fetchServer()
    }

    Column {
        MainNavigator("Home") {
            Rectangle(DpSize(42.dp, 42.dp),
                Modifier.clickable { showBottomSheet = true }.clip(CircleShape)
                    .background(MaterialTheme.colors.secondary))
        }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(SpecificConfiguration.defaultContentPadding)
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

            ColumnRoundedContainer(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                LargeButton(
                    text = "Set notification",
                    colors = SurfaceColors.secondaryButtonColors,
                    clip = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NotificationViewModel.pushNotification(generateNotificationData())
                }

                LargeButton(
                    text = "Set snap alert",
                    colors = SurfaceColors.secondaryButtonColors,
                    clip = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SnapAlertViewModel.pushSnapAlert(generateSnapAlertData())
                }

                LargeButton(
                    text = "Experimental Functions",
                    clip = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
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
                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
                    .padding(SpecificConfiguration.defaultContentPadding)
            ) {
                SpaceXLauncherHistory()
            }
        }
    }
}

class HomeScreenViewModel(private val id: Uuid = uuid4()) : ViewModel() {
    companion object {
        private var _isFirstVisit = false
    }

    init {
        println("${LocalDateTime.now()} - Home screen view model online: $id")
    }

    val isFirstVisit get() = _isFirstVisit

    override fun onCleared() {
        println("${LocalDateTime.now()} - Home screen view model offline: $id")
        super.onCleared()
    }

    @Throws(Throwable::class)
    suspend fun fetchServer() {
        // Update visibility.
        _isFirstVisit = true
        try {
            val result = Apis.getServerConnection()
            println(result.toString())
            NotificationViewModel.pushNotification(MutableNotificationData(
                "Server", result.main
            ) { it() })
        } catch (e: Exception) {
            println("${LocalDateTime.now()} - Error: ${e.message}")
            NotificationViewModel.pushNotification(e)
        }
    }
}