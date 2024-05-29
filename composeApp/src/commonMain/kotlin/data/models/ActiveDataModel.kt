package data.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import components.ColorAssets
import components.SurfaceColors
import io.ktor.http.Url
import kotlinx.datetime.LocalDateTime

/// Snap Alert

private val SurfaceColors.Companion.defaultSnapAlertColors: SurfaceColors
    get() = SurfaceColors(ColorAssets.ForegroundColor, ColorAssets.Surface, ColorAssets.Background)

/**
 * Data class for common snap alert driver.
 * @author Lester E
 *
 * @param id [Uuid] The unique identifier for the alert.
 * @param message [String] The message to display.
 * @param surface [SurfaceColors] The colors to use for the alert's surface.
 * @param trailing [Function] The trailing content to display.
 */
data class SnapAlertData(
    val id: Uuid,
    val message: String,
    val surface: SurfaceColors = SnapAlertData.defaultSurface,
) {
    /**
     * Convenient constructor for common snap alert driver.
     *
     * @param message [String] The message to display.
     * @param trailing [Function] The trailing content to display.
     */
    constructor(message: String) : this(uuid4(), message)

    companion object

    var launchTime: LocalDateTime? = null
}

/** Default snap alert surface assets. */
val SnapAlertData.Companion.defaultSurface: SurfaceColors
    get() = SurfaceColors.defaultSnapAlertColors


/// Notification

/**
 * Notification alert level.
 * @author Lester E
 */
enum class NotificationLevel {
    /** Notification marked as normal will operate automatically by system */
    NORMAL,

    /** Notification marked as permanently will not be removed automatically by system */
    PERMANENTLY;
}

private val SurfaceColors.Companion.defaultNotificationColors: SurfaceColors
    get() = SurfaceColors(ColorAssets.ForegroundColor, ColorAssets.Surface, ColorAssets.Background)


/**
 * Data class for mutable notification.
 * @author Lester E
 *
 * @param id [Uuid] Generated will automatically in convenient constructor, the format is UUID.
 * @param title [String] Title of notification.
 * @param message [String] Message of notification.
 * @param level [NotificationLevel] Level of notification.
 * @param colors [SurfaceColors] Colors of notification.
 * @param image [Url] Image url of notification.
 * @param onClick [Function] ...
 *
 * @property notificationLevel [MutableState] Alert level of notification.
 */
data class MutableNotificationData(
    val id: Uuid,
    val title: String,
    val message: String,
    private val level: NotificationLevel = NotificationLevel.NORMAL,
    val colors: SurfaceColors = MutableNotificationData.defaultSurface,
    val image: Url? = null,
    /**
     * Function that will be called when notification is clicked.
     * Please make sure the caller will call the function `destroy` to destroy the notification if necessary.
     */
    val onClick: suspend (destroy: () -> Unit) -> Unit,
) {
    /**
     * Convenient constructor for normal notification
     *
     * @param title [String] Title of notification
     * @param message [String] Message of notification
     * @param image [Url] Image url of notification
     * @param onClick [Function] Function that will be called when notification is clicked
     */
    constructor(
        title: String, message: String, image: Url? = null, onClick: suspend (destroy: () -> Unit) -> Unit
    ) : this(uuid4(), title, message, image = image, onClick = onClick)

    val notificationLevel: MutableState<NotificationLevel> = mutableStateOf(level)

    companion object
}

/** Default notification surface assets. */
val MutableNotificationData.Companion.defaultSurface: SurfaceColors
    get() = SurfaceColors.defaultNotificationColors
