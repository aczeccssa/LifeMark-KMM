package data.models

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import components.ColorAssets
import components.SurfaceColors
import data.platform.generateUUID
import io.ktor.http.Url
import kotlinx.serialization.Serializable

/// Snap Alert

private val SurfaceColors.Companion.defaultSnapAlertColors: SurfaceColors
    get() = SurfaceColors(ColorAssets.ForegroundColor, ColorAssets.Surface, ColorAssets.Background)

/**
 * Data class for common snap alert driver.
 * @author Lester E
 *
 * @param id [String] The unique identifier for the alert.
 * @param message [String] The message to display.
 * @param surface [SurfaceColors] The colors to use for the alert's surface.
 * @param modifier [Modifier] The modifier to apply to the alert.
 * @param trailing [Function] The trailing content to display.
 */
data class SnapAlertData(
    val id: String,
    val message: String,
    val surface: SurfaceColors = SnapAlertData.defaultSurface,
    val modifier: Modifier = Modifier,
    val trailing: @Composable () -> Unit
) {
    /**
     * Convenient constructor for common snap alert driver.
     *
     * @param message [String] The message to display.
     * @param modifier [Modifier] The modifier to apply to the alert.
     * @param trailing [Function] The trailing content to display.
     */
    constructor(
        message: String, modifier: Modifier, trailing: @Composable () -> Unit
    ) : this(generateUUID(), message, modifier = modifier, trailing = trailing)

    companion object
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
 * @param id [String] Generated will automatically in convenient constructor, the format is UUID.
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
    val id: String,
    val title: String,
    val message: String,
    private val level: NotificationLevel = NotificationLevel.NORMAL,
    val colors: SurfaceColors = MutableNotificationData.defaultSurface,
    val image: Url,
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
        title: String, message: String, image: Url, onClick: suspend (destroy: () -> Unit) -> Unit
    ) : this(generateUUID(), title, message, image = image, onClick = onClick)

    val notificationLevel: MutableState<NotificationLevel> = mutableStateOf(level)

    companion object
}

/** Default notification surface assets. */
val MutableNotificationData.Companion.defaultSurface: SurfaceColors
    get() = SurfaceColors.defaultNotificationColors
