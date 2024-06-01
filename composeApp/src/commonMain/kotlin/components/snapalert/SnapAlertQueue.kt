package components.snapalert

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import data.SpecificConfiguration
import data.models.SnapAlertData
import data.units.now
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import screens.NAVIGATION_BAR_HEIGHT
import viewmodel.SnapAlertViewModel

@Composable
fun SnapAlertQueue() {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().zIndex(2f).navigationBarsPadding()
            .padding(bottom = NAVIGATION_BAR_HEIGHT + 8.dp)
            .padding(horizontal = SpecificConfiguration.defaultContentPadding)
    ) {
        SnapAlertViewModel.queue.reversed().forEach { data -> // Reversed to show!!!
            SnapAlertQueueItem(data)
        }
    }
}

@Composable
private fun SnapAlertQueueItem(data: SnapAlertData) {
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds
    var isDestroyable by remember { mutableStateOf(true) }
    val snapAlertOffsetY = animateDpAsState(
        targetValue = if (isDestroyable) screenSize.height / 2 else 0.dp,
        animationSpec = tween(durationMillis = SnapAlertViewModel.ANIMATION_DURATION.toInt())
    )
    val componentAlpha = animateFloatAsState(
        targetValue = if (isDestroyable) 0f else 1f,
        animationSpec = tween(durationMillis = SnapAlertViewModel.ANIMATION_DURATION.toInt())
    )

    fun destroyHandler() {
        isDestroyable = true
        SnapAlertViewModel.destroySnapAlert(data)
    }

    LaunchedEffect(Unit) {
        println("${LocalDateTime.now()} - Snap alert compose launch: ${data.id}, queue size: ${SnapAlertViewModel.queue.size}")
        isDestroyable = false
        delay(SnapAlertViewModel.LIFE_CYCLE_TIMEOUT)
        destroyHandler()
    }

    Row(
        modifier = Modifier
            .alpha(componentAlpha.value)
            .offset(y = snapAlertOffsetY.value)
            .padding(0.dp, 6.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SnapAlert(data) { destroyHandler() }
    }
}