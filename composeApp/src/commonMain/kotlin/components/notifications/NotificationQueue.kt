package components.notifications

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.SpecificConfiguration
import data.models.MutableNotificationData
import data.models.NotificationLevel
import data.units.now
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import viewmodel.NotificationViewModel

@Composable
fun NotificationQueue() {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        NotificationViewModel.notificationQueue.forEach {
            NotificationQueueItem(it)
        }
    }
}

@Composable
private fun NotificationQueueItem(data: MutableNotificationData) {
    val statusBarHeight = SpecificConfiguration.edgeSafeArea.asPaddingValues().calculateTopPadding()
    val isShow = remember { mutableStateOf(false) }

    val animationDuration = NotificationViewModel.ANIMATION_DURATION
    val paddingTop = maxNotificationContainerSize + statusBarHeight
    val containerOffset = animateDpAsState(
        targetValue = if (isShow.value) statusBarHeight else -paddingTop,
        animationSpec = tween(durationMillis = animationDuration.toInt())
    )
    val containerHeight = animateDpAsState(
        targetValue = if (isShow.value) maxNotificationContainerSize else 0.dp,
        animationSpec = tween(durationMillis = animationDuration.toInt())
    )

    suspend fun destroyHandle(item: MutableNotificationData) {
        if (item.notificationLevel.value === NotificationLevel.NORMAL) {
            delay(NotificationViewModel.LIFECYCLE)
            if (item.notificationLevel.value === NotificationLevel.NORMAL) {
                NotificationViewModel.destroyNotification(item)
            }
        }
    }

    LaunchedEffect(Unit) {
        // MARK: `println` function is used to replace the `Log.d(tag, message)` in the Kotlin Multiplatform project.
        println("${LocalDateTime.now()} - Notification current: ${data.id}")
        isShow.value = true
    }

    LaunchedEffect(Unit) {
        val notificationToDestroy = when {
            NotificationViewModel.activeNotification.value === data -> NotificationViewModel.activeNotification.value
            NotificationViewModel.passedNotification.value === data -> NotificationViewModel.passedNotification.value
            else -> null
        }

        notificationToDestroy?.let { destroyHandle(it) }
    }

    LaunchedEffect(NotificationViewModel.activeNotification.value) {
        if (NotificationViewModel.activeNotification.value !== data) {
            isShow.value = false
            delay(animationDuration)
        }
    }

    Column(Modifier.offset(y = containerOffset.value).heightIn(max = containerHeight.value)) {
        MutableNotification(data)
    }
}