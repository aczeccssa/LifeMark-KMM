package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import data.models.MutableNotificationData
import data.models.NotificationLevel
import data.units.now
import io.ktor.http.Url
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime

/**
 * Notification view model
 *
 * This view model is responsible for managing the notification queue and the active notification.
 *
 * @property notificationQueue [MutableList] The queue of notifications.
 * @property activeNotification [MutableState] Current showing notification.
 * @property passedNotification [MutableState] Previous showing notification.
 *
 * @property ANIMATION_DURATION [Long] The notification enter and exit animation duration.
 * @property LIFECYCLE [Long] The notification automatically destroyed after this duration.
 */
object NotificationViewModel {
    val notificationQueue: MutableList<MutableNotificationData> = mutableStateListOf()

    val activeNotification: MutableState<MutableNotificationData?> = mutableStateOf(null)
    val passedNotification: MutableState<MutableNotificationData?> = mutableStateOf(null)

    const val ANIMATION_DURATION = 600L // 0.6 seconds
    const val LIFECYCLE = 6000L // 10 seconds

    /**
     * Add a new notification to the queue.
     * @param notification [MutableNotificationData] The notification to be added.
     */
    fun pushNotification(notification: MutableNotificationData) {
        // NARK: Made current active notification passed
        passedNotification.value = activeNotification.value

        // MARK: Set active notification to the first notification in the queue
        notificationQueue.add(notification)
        activeNotification.value = notification
    }

    /**
     * Remove a notification from the queue.
     * @param notification [MutableNotificationData] The notification to be removed.
     */
    suspend fun destroyNotification(notification: MutableNotificationData) {
        activeNotification.value = null
        delay(ANIMATION_DURATION - 100)
        notificationQueue.remove(notification)
        println("${LocalDateTime.now()} - Notification destroyed: ${notification.id}")
    }

    /**
     * Set a notification as `PERMANENTLY` level.
     * @param source [MutableNotificationData] The notification witch will set to `PERMANENTLY`.
     */
    fun madeNotificationPermanently(source: MutableNotificationData) {
        val index = notificationQueue.indexOf(source)
        if (index == -1) return
        notificationQueue[index].notificationLevel.value = NotificationLevel.PERMANENTLY
    }

    // Coroutine Scope for this view model only.
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun pushExceptionNotification(
        exception: Throwable, defaultMsg: String? = null, onClick: suspend () -> Unit = { }
    ) {
        val notification = MutableNotificationData("Exception",
            exception.message ?: (defaultMsg ?: "Unknown exception"),
            Url(""),
            onClick = { onClick() })
        this.pushNotification(notification)
    }
}