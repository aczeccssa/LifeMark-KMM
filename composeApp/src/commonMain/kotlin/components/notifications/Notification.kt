package components.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import components.ColorAssets
import components.SurfaceColors
import data.SpecificConfiguration
import data.models.MutableNotificationData
import data.models.NotificationLevel
import io.ktor.http.Url

/**
 * Immutable Notification Component
 *
 * @param title [String] Title of notification.
 * @param message [String] Message of notification.
 * @param background [Color] Notification background color.
 * @param foreground [Color] Notification foreground color, enable in text.
 * @param trailing [(@Composable () -> Unit)?] Optional, trailing component.
 *
 * @author Lester E
 */
@Composable
fun Notification(
    title: String,
    message: String,
    background: Color = MaterialTheme.colors.surface,
    foreground: Color = MaterialTheme.colors.onSurface,
    trailing: (@Composable () -> Unit)? = null
) {
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds
    val horizontalSafePadding = SpecificConfiguration.edgeSafeArea.asPaddingValues()
        .calculateLeftPadding(LayoutDirection.Ltr)
    val containerShape = RoundedCornerShape(18.dp)
    val trailingLimitSize = 52.dp
    val containerPadding = 12.dp
    val contentOffset = 2.dp
    val contentSpace = 4.dp
    val titleStyle = TextStyle(
        fontSize = 15.sp, lineHeight = 15.sp, fontWeight = FontWeight.Medium, color = foreground
    )
    val messageStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 13.sp,
        fontWeight = FontWeight.Normal,
        color = foreground.copy(alpha = 0.8f)
    )

    LaunchedEffect(Unit) {
        println("Horizontal safe edge: $horizontalSafePadding")
    }

    Box(
        modifier = Modifier.zIndex(4f)
            .shadow(elevation = 12.dp, containerShape, spotColor = ColorAssets.SurfaceShadow.value)
            .width(screenSize.width - 2 * horizontalSafePadding).clip(containerShape)
            .background(background).padding(containerPadding)
    ) {
        // Contents
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = contentOffset)
            ) {
                // Title of this notification
                Text(title, style = titleStyle, maxLines = 1, overflow = TextOverflow.Ellipsis)

                // Operator between these component
                Spacer(Modifier.height(contentSpace))

                // Message of this notification
                Text(message, style = messageStyle, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Spacer(Modifier.width(horizontalSafePadding))

            trailing?.let {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.heightIn(max = trailingLimitSize)
                        .widthIn(max = trailingLimitSize)
                ) { it() }
            }
        }
    }
}