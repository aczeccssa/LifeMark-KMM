package screens.experimental

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
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
import components.LargeButton
import components.navigator.MainNavigator
import data.models.SnapAlertData
import data.units.TrackTimer
import viewmodel.NotificationViewModel
import viewmodel.SnapAlertViewModel

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

    val timerRunningState by remember { timer.isRunning }

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
        MainNavigator(Icons.Rounded.Star, "Track Timer(Dev)")

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = if (timerRunningState) "Timer running..."
            else {
                diff?.let { "Duration: ${diff.toString()}ms" } ?: "Click start to launch timer"
            }, color = if (expired) Color.Red else Color.Green)

            Spacer(Modifier.height(12.dp))

            // DatePicker()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.clip(RoundedCornerShape(24.dp)).fillMaxWidth(0.7f)
                    .border(2.dp, MaterialTheme.colors.primary, RoundedCornerShape(24.dp))
                    .padding(24.dp, 18.dp)
            ) {
                BasicTextField(
                    expectedDuration.toString(),
                    onValueChange = { expectedDuration = it.toInt(10000) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                )
            }

            Spacer(Modifier.height(12.dp))

            LargeButton(
                text = "${if (timerRunningState) "Stop" else "Start"} Timer",
                modifier = Modifier.fillMaxWidth(0.7f).padding(vertical = 8.dp)
            ) { timerRunHandle() }
        }
    }
}