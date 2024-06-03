package screens.experimental

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import components.ColorAssets
import components.ColumnRoundedContainer
import components.LargeButton
import components.navigator.MainNavigator
import components.properties
import data.SpecificConfiguration
import data.models.SnapAlertData
import data.units.TrackTimer
import data.units.fromEpochMilliseconds
import data.units.now
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import viewmodel.NotificationViewModel
import viewmodel.SnapAlertViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackTimerTest() {
    var diff: Int? by remember { mutableStateOf(null) }
    var expectedDuration by remember { mutableStateOf(10000) }
    var expired by remember { mutableStateOf(false) }
    val timer: TrackTimer by remember {
        mutableStateOf(TrackTimer.create(expectedDuration.toLong()) {
            diff = it.duration.toInt() // Differences.
            expired = diff!!.toLong() > expectedDuration // Is expired?
        })
    }
    val scrollState = rememberScrollState()

    val timerRunningState by remember { timer.isRunning }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds()
    )

    DisposableEffect(Unit) {
        onDispose {
            // Close timer when this scope is disposed.
            if (timerRunningState) {
                timer.dispose()
                // Remind user timer end and the result.
                SnapAlertViewModel.pushSnapAlert(SnapAlertData("Timer duration: ${diff.toString()}ms"))
            }
            // Clear timer.
            timer.reset()
        }
    }

    fun timerRunHandle() {
        if (timerRunningState) {
            try {
                timer.dispose()
            } catch (e: Throwable) {
                NotificationViewModel.pushNotification(e)
            }
        } else {
            timer.reset()
            timer.enableDefaultLogoutAction = true
            timer.launch()
        }
    }

    // View
    Column {
        MainNavigator("Track Timer Test")

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(scrollState).fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(SpecificConfiguration.defaultContentPadding)
        ) {
            ColumnRoundedContainer {
                DatePicker(
                    state = datePickerState,
                    colors = MaterialTheme.properties.defaultDatePickerColors,
                    title = {
                    datePickerState.selectedDateMillis?.let {
                        Text("Selected date: ${LocalDateTime.fromEpochMilliseconds(it)}")
                    }?: Text("Nothing been selected.")
                })
            }

            Spacer(Modifier.height(24.dp))

            ColumnRoundedContainer(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = if (timerRunningState) "Timer running..." else {
                    diff?.let { "Duration: ${diff.toString()}ms" } ?: "Click start to launch timer"
                }, color = if (expired) Color.Red else Color.Green)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.clip(RoundedCornerShape(24.dp)).fillMaxWidth()
                        .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(24.dp))
                        .padding(24.dp, 8.dp)
                ) {
                    BasicTextField(
                        expectedDuration.toString(),
                        onValueChange = { expectedDuration = it.toInt(10000) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.subtitle1.copy(color = MaterialTheme.colors.primary),
                    )
                }

                LargeButton(
                    text = "${if (timerRunningState) "Stop" else "Start"} Timer",
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) { timerRunHandle() }
            }
        }
    }
}