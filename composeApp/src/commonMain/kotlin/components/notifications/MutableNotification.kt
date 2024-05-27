package components.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import components.ColorAssets
import data.SpecificConfiguration
import data.models.MutableNotificationData
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.delay
import viewmodel.NotificationViewModel

// MARK: MutableNotification Only ⬇ ️
private const val maxMessageLine = 5
private val trailingLimitSize = 52.dp
private val containerPadding = 12.dp
private val contentOffset = 2.dp
private val contentSpace = 4.dp
private val messageTitleFontSize = 15.sp
private val messageTitleLineHeight = 17.sp
private val messageContentFontSize = 13.sp
private val messageContentLineHeight = 15.sp
private val contentFeatherweight = 80.dp
val maxNotificationContainerSize =
    containerPadding + contentOffset + contentFeatherweight + messageTitleLineHeight.value.dp + contentSpace + maxMessageLine * messageContentLineHeight.value.dp + contentOffset + containerPadding
val minNotificationContainerSize = containerPadding + trailingLimitSize + containerPadding
val dragSwitchFolderOffsetThreshold = minNotificationContainerSize.value / 2
private val containerShape = RoundedCornerShape(18.dp)

/**
 * Mutable Notification Component
 *
 * This component will automatic run when without any out-force:
 *     Notification will automatic appear and disappear after `NotificationViewModel.LIFECYCLE`
 *     times if the notification level is `NORMAL`.
 *     You can drag to open this notification, the level of the notification will change to
 *     `PERMANENTLY` and waiting for your hand drag up to fold it, and close it automatically.
 *
 * - Closed
 * ```plaintext
|—————————————— ScreenWidth - SafeArea ——————————————|
╔════════════════════════════════════════════════════╗ ——————
║ ————————————————————                        ╔════╗ ║   |
║ ————————————————————————————————————————    ║    ║ ║ 076.dp
║ ———————————————                             ╚════╝ ║   |
╚════════════════════════════════════════════════════╝ ——————
 * ```
 * - Expanded
 * ```plaintext
╔════════════════════════════════════════════════════╗ ——————
║                                                    ║   |
║                                                    ║   |
║                                                    ║   |
║                                                    ║   |
║                                                    ║   |
║                                                    ║   |
║ ————————————————————                               ║ 204.dp
║ —————————————————————————————————————————————————— ║   |
║ —————————————————————————————————————————————————— ║   |
║ —————————————————————————————————————————————————— ║   |
║ ———————————————————————————————————                ║   |
║ —————————————————————————————————————————————————— ║   |
║ ————————————                                       ║   |
╚════════════════════════════════════════════════════╝ ——————
|—————————————— ScreenWidth - SafeArea ——————————————|
 * ```
 *
 * @param data [MutableNotificationData] Data which can drive the component
 *
 * @author Lester E
 */
@Composable
fun MutableNotification(
    data: MutableNotificationData,
    viewModel: MutableNotificationViewModel = viewModel { MutableNotificationViewModel(data) }
) {
    // Properties
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds
    val horizontalSafePadding = SpecificConfiguration.edgeSafeArea.asPaddingValues()
        .calculateLeftPadding(LayoutDirection.Ltr)
    val imageResource = asyncPainterResource(data = data.image)
    val titleStyle = TextStyle(
        fontSize = messageTitleFontSize,
        lineHeight = messageContentLineHeight,
        fontWeight = FontWeight.SemiBold,
        color = data.colors.foreground.value,
    )
    val messageStyle = TextStyle(
        fontSize = messageContentFontSize,
        lineHeight = messageTitleLineHeight,
        fontWeight = FontWeight.Normal,
        letterSpacing = 1.sp,
        color = data.colors.foreground.value.copy(alpha = 0.9f)
    )

    // Mutable states
    val expanded by remember { viewModel.expanded }
    val isOpened by remember { viewModel.isOpened }

    // Animate States
    val trailingImageWidth = animateDpAsState(
        targetValue = if (expanded) 0.dp else trailingLimitSize,
        animationSpec = tween(durationMillis = NotificationViewModel.ANIMATION_DURATION.toInt())
    )
    val messageContentMaxLine = animateIntAsState(
        targetValue = if (expanded) maxMessageLine else 1,
        animationSpec = tween(durationMillis = NotificationViewModel.ANIMATION_DURATION.toInt())
    )
    val backgroundImageMuskAlpha = animateFloatAsState(
        targetValue = if (expanded) 0.5f else 1f,
        animationSpec = tween(durationMillis = NotificationViewModel.ANIMATION_DURATION.toInt())
    )

    // Drag component
    val containerDragState = rememberDraggableState { newValue ->
        // FIXME: Not allow change when drag up but closed or drag down but open
        viewModel.dragOffsetProcessor(newValue)
    }

    // Segmented
    val destroyCheckReboundsDelay = 200L
    LaunchedEffect(expanded) {
        if (!expanded && isOpened) {
            delay(destroyCheckReboundsDelay)
            if (!expanded && isOpened) viewModel.destroySelf()
        }
    }

    // Animated main box height
    val boxHeight = animateDpAsState(
        targetValue = if (expanded) maxNotificationContainerSize else minNotificationContainerSize,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
    )


    // Contents
    Box(modifier = Modifier.zIndex(4f)
        .shadow(elevation = 18.dp, containerShape, spotColor = ColorAssets.SurfaceShadow.value)
        .width(screenSize.width - 2 * horizontalSafePadding).heightIn(max = boxHeight.value)
        .clip(containerShape).background(data.colors.surface.value)
        .draggable(state = containerDragState,
            orientation = Orientation.Vertical,
            onDragStarted = { },
            onDragStopped = { }).clickable { viewModel.containerClickAction() }) {
        Box(Modifier.clip(containerShape).background(MaterialTheme.colors.error.copy(alpha = 0f))) {
            KamelImage(
                resource = imageResource,
                contentDescription = "Random image test header",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Opacity musk
            Box(
                Modifier.fillMaxSize().background(MaterialTheme.colors.surface)
                    .alpha(backgroundImageMuskAlpha.value)
            ) { }
        }

        // Contents
        Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
            AnimatedVisibility(expanded) {
                Spacer(Modifier.heightIn(max = contentFeatherweight).weight(1f))
            }

            // Title and message
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(containerPadding)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = contentOffset)
                ) {
                    // Title of this notification
                    Text(
                        data.title,
                        style = titleStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Operator between these component
                    Spacer(Modifier.height(contentSpace))

                    // Message of this notification
                    Text(
                        text = data.message,
                        style = messageStyle,
                        maxLines = messageContentMaxLine.value,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.width(horizontalSafePadding))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.heightIn(max = trailingLimitSize)
                        .widthIn(max = trailingImageWidth.value)
                ) {
                    KamelImage(
                        resource = imageResource,
                        contentDescription = "Random image test header",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().weight(1f).clip(CircleShape)
                            .background(MaterialTheme.colors.error)
                    )
                }
            }
        }
    }
}
