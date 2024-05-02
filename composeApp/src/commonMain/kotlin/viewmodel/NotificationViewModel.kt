package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import composes.notifications.MutableNotificationData
import composes.notifications.NotificationStatus
import kotlinx.coroutines.delay

object NotificationViewModel {
    val notificationQueue: MutableList<MutableNotificationData> = mutableStateListOf()

    val activeNotification: MutableState<MutableNotificationData?> = mutableStateOf(null)
    val passedNotification: MutableState<MutableNotificationData?> = mutableStateOf(null)

    const val ANIMATION_DURATION = 600L // 0.6 seconds
    const val LIFECYCLE = 6000L // 10 seconds

    fun pushNotification(notification: MutableNotificationData) {
        // NARK: Made current active notification passed
        passedNotification.value = activeNotification.value

        // MARK: Set active notification to the first notification in the queue
        notificationQueue.add(notification)
        activeNotification.value = notification
    }

    suspend fun destroyNotification(notification: MutableNotificationData) {
        activeNotification.value = null
        delay(ANIMATION_DURATION - 100)
        notificationQueue.remove(notification)
        println("${notification.id} has been destroyed")
    }

    fun madeNotificationPermanently(source: MutableNotificationData) {
        val index = notificationQueue.indexOf(source)

        if (index == -1) return
        notificationQueue[index].notificationStatus.value = NotificationStatus.PERMANENTLY
    }
}