package data.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import components.ColorAssets
import components.SurfaceColors
import io.ktor.http.Url

enum class NotificationStatus {
    NORMAL, PERMANENTLY
}

private val SurfaceColors.Companion.defaultNotificationColors: SurfaceColors
    get() = SurfaceColors(ColorAssets.ForegroundColor, ColorAssets.Surface, ColorAssets.Background)


data class MutableNotificationData(
    val id: String,
    val title: String,
    val message: String,
    private val status: NotificationStatus = NotificationStatus.NORMAL,
    val colors: SurfaceColors = SurfaceColors.defaultNotificationColors,
    val image: Url,
    val onClick: (destroy: () -> Unit) -> Unit,
) {
    val notificationStatus: MutableState<NotificationStatus> = mutableStateOf(status)
}
