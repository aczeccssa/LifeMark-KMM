package components.snapalert

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.models.SnapAlertData
import kotlinx.coroutines.delay
import viewmodel.SnapAlertViewModel

@Composable
fun SnapAlertQueue() {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        SnapAlertViewModel.queue.reversed().forEach { // Reversed to show!!!
            // TODO: Implement the actual alert
            SnapAlertQueueItem(it)
        }
    }
}

@Composable
private fun SnapAlertQueueItem(data: SnapAlertData) {
    var isDestroyable by remember { mutableStateOf(false) }
    val snapAlertOffsetX = animateDpAsState(
        targetValue = if (isDestroyable) 50.dp else 0.dp,
        animationSpec = tween(durationMillis = SnapAlertViewModel.ANIMATION_DURATION.toInt())
    )

    LaunchedEffect(Unit) {
        delay(SnapAlertViewModel.LIFE_CYCLE_TIMEOUT)
        isDestroyable = true
        SnapAlertViewModel.destroySnapAlert(data)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.offset(y = snapAlertOffsetX.value).padding(0.dp, 6.dp)
    ) { SnapAlert(data) }
}