package screens.experimental

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import cafe.adriel.voyager.core.screen.Screen
import components.ColorAssets
import components.NavigationHeader
import components.SecondaryLargeButton
import components.SourceMutableImagePreview
import components.message.AcceptHandle
import components.message.Message
import components.message.MessageHandle
import components.message.rememberMessageState
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Layers
import compose.icons.evaicons.fill.List
import compose.icons.evaicons.fill.MenuArrow
import compose.icons.evaicons.outline.Cube
import compose.icons.evaicons.outline.Trash2
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import data.roundToIntOffset
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlinx.coroutines.launch
import utils.MediaLinkCache
import viewmodel.SnapAlertViewModel
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object ExperimentalImageSetScreen : Screen {
    // The data class for image set, as an item.
    private data class ImageSetPair(val url: Url, var scale: Float)

    // The max image count in one image set.
    private const val THRESHOLD = 8

    // Animation properties
    private const val ANIMATION_DAMPING = Spring.DampingRatioLowBouncy
    private const val ANIMATION_STIFFNESS = Spring.StiffnessLow

    // Main component.
    @Composable
    override fun Content() {
        // Static properties.
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight
        val imageSize = androidx.compose.ui.unit.min(SpecificConfiguration.localScreenConfiguration.bounds.width * 0.9f, 520.dp)
        // States for ui driver.
        val imageList: MutableList<ImageSetPair> = remember { mutableStateListOf() }
        val showPicturePreview = remember { mutableStateOf(false) }
        var picturePreviewInitIndex by remember { mutableStateOf(0) }

        // Control mode for image list transform.
        var setTransform by remember { mutableStateOf(true) }

        // Scroll state and message state.
        val messageState = rememberMessageState(title = "Alert",
            message = "Are you sure to clean this image list?!",
            acceptHandle = AcceptHandle("Clean") { imageList.clear() },
            cancelHandle = MessageHandle("Cancel") { })
        val verticalScrollState = rememberScrollState()
        val scope = rememberCoroutineScope()

        // Image offset properties.
        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        val animatedOffset = animateOffsetAsState(
            Offset(offsetX, offsetY), spring(ANIMATION_DAMPING, ANIMATION_STIFFNESS)
        )
        val offsetYThreshold = SpecificConfiguration.localScreenConfiguration.bounds.width.value * 0.5f

        // Image list angle update handle.
        fun updateScaleAngle() = imageList.forEach { it.scale = randomAngle() }

        // Update each scale angle when transform mode is changed.
        LaunchedEffect(setTransform) { updateScaleAngle() }

        // Update each scale angle when image list has changed.
        LaunchedEffect(imageList.size) { updateScaleAngle() }

        // Main contents
        Surface {
            NavigationHeader("Peekaboo Picker", NavigationHeaderConfiguration.clearConfiguration) {
                Row(Modifier, Arrangement.spacedBy(12.dp)) {
                    AnimatedVisibility(imageList.isNotEmpty()) {
                        Icon(
                            imageVector = EvaIcons.Outline.Trash2,
                            contentDescription = null,
                            tint = ColorAssets.Red.value,
                            modifier = Modifier.clickable(
                                onClick = { messageState.launch() },
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ).height(23.dp)
                        )
                    }

                    val transformSwitchColor = animateColorAsState(
                        targetValue = if (setTransform) ColorAssets.Green.value else ColorAssets.SK.FillBlue.value,
                        animationSpec = tween(ANIMATION_DAMPING.toInt())
                    )
                    Icon(
                        imageVector = if (setTransform) EvaIcons.Outline.Cube else EvaIcons.Fill.MenuArrow,
                        contentDescription = null,
                        tint = transformSwitchColor.value,
                        modifier = Modifier.clickable(
                            onClick = { setTransform = !setTransform },
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ).height(23.dp)
                    )
                }
            }

            // Mutable image list component:
            Column(
                Modifier.fillMaxSize().verticalScroll(verticalScrollState, enabled = !setTransform)
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding).padding(
                        top = topOffset, bottom = SpecificConfiguration.defaultContentPadding + 8.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Open image list preview handle on this box element, only list not empty and transform mode can open.
                Box(Modifier.padding(top = 12.dp).clickable(MutableInteractionSource(), null) {
                    if (imageList.isNotEmpty() && setTransform) showPicturePreview.value = true
                }) {
                    // Image list.
                    if (imageList.isEmpty()) { // Icon placeholder when list empty.
                        Icon(
                            if (setTransform) EvaIcons.Fill.Layers else EvaIcons.Fill.List,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp),
                            tint = ColorAssets.LMPurple.value.copy(alpha = 0.1f)
                        )
                    } else { // Image list.
                        imageList.forEachIndexed { index, pair ->
                            // Update current index when image list changed, for preview current image on appear.
                            if (index == max(imageList.size - 1, 0)) {
                                picturePreviewInitIndex = imageList.indexOf(pair)
                            }
                            BoxingImage(pair, 120.dp, imageSize, index, setTransform, 12.dp, Modifier
                                .offset {
                                    // Calc offset declare with index.
                                    val diff = (index + 1f) / imageList.size
                                    Offset(animatedOffset.value.x * diff, animatedOffset.value.y * diff).roundToIntOffset()
                                }.then(if (setTransform) {
                                    Modifier.pointerInput(Unit) {
                                        detectDragGestures(
                                            onDragEnd = {
                                                // Update transform mode and reset offset anyways.
                                                if (offsetY > offsetYThreshold) setTransform = false
                                                offsetY = 0f
                                                offsetX = 0f
                                            }
                                        ) { change, _ ->
                                            // Update offset x and y when drag change with transform mode.
                                            offsetX += change.position.x - change.previousPosition.x
                                            offsetY += change.position.y - change.previousPosition.y
                                        }
                                    }
                                } else Modifier)) // Use then each for transform mode drag and apply scroll when list mode.
                        }
                    }
                }
            }

            // Bottom button for append image.
            Column(
                Modifier.fillMaxSize()
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding)
                    .padding(bottom = SpecificConfiguration.defaultContentPadding + 8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                AnimatedVisibility(setTransform) {
                    SecondaryLargeButton("Add Picture", RoundedCornerShape(12.dp)) {
                        if (imageList.size < THRESHOLD) {
                            scope.launch {
                                imageList.add(ImageSetPair(MediaLinkCache.randomImage(), randomAngle()))
                                verticalScrollState.animateScrollTo(verticalScrollState.maxValue)
                            }
                        } else {
                            SnapAlertViewModel.push("You can only add up to $THRESHOLD images!")
                        }
                    }
                }
            }

            // Message component.
            Message(messageState)
        }

        // Image list preview component.
        SourceMutableImagePreview(
            state = showPicturePreview,
            list = imageList.map { it.url },
            sourceSize = Size(imageSize.value, imageSize.value),
            sourceOffset = Offset(SpecificConfiguration.defaultContentPadding.value, topOffset.value),
            initIndex = max(min(picturePreviewInitIndex, 0), imageList.size - 1)
        )
    }

    // Angle pool for no-repeat random angle.
    private val anglePool = mutableListOf<Float>()

    // Generate no-repeat random angle by the `anglePool`.
    private fun randomAngle(min: Float = -30f, max: Float = 30f): Float {
        if (anglePool.size > THRESHOLD) anglePool.clear()
        var angle: Float
        var times = 0
        do {
            times++
            angle = Random.nextFloat() * (max - min) + min
            if (times > 5) break
        } while (anglePool.contains(angle))
        anglePool.add(angle)
        return angle
    }

    // Animated image component.
    @Composable
    private fun BoxingImage(cfg: ImageSetPair, baseSize: Dp, transformSize: Dp, index: Int, transform: Boolean, spacing: Dp, modifier: Modifier = Modifier) {
        var launch by remember { mutableStateOf(false) }

        val alpha = animateFloatAsState(
            if (launch) 1f else 0f, spring(ANIMATION_DAMPING, ANIMATION_STIFFNESS)
        )
        val imageHeight = animateDpAsState(
            targetValue = if (transform) transformSize else baseSize,
            animationSpec = spring(ANIMATION_DAMPING, ANIMATION_STIFFNESS)
        )
        val imageWith = animateDpAsState(
            targetValue = if (transform) transformSize else SpecificConfiguration.localScreenConfiguration.bounds.width - 2 * SpecificConfiguration.defaultContentPadding,
            animationSpec = spring(ANIMATION_DAMPING, ANIMATION_STIFFNESS)
        )
        // With launch
        val imageScale = animateFloatAsState(
            targetValue = if (transform && !launch) 0.3f else if (transform && launch) 0.7f else 1f,
            animationSpec = spring(ANIMATION_DAMPING, ANIMATION_STIFFNESS)
        )
        val angle = animateFloatAsState(
            targetValue = if (transform && !launch) listOf(randomAngle(270f, 480f), randomAngle(-270f, -480f)).random() else if (transform && launch) cfg.scale else 0f,
            animationSpec = spring(ANIMATION_DAMPING, ANIMATION_STIFFNESS)
        )
        val imageOffsetY = animateDpAsState(
            targetValue = if (!launch) SpecificConfiguration.localScreenConfiguration.bounds.height + index * (baseSize + spacing) else if (transform && launch) 0.dp else index * (baseSize + spacing),
            animationSpec = spring(ANIMATION_DAMPING, ANIMATION_STIFFNESS)
        )

        LaunchedEffect(Unit) {
            if (transform) launch = true
        }

        KamelImage(
            resource = asyncPainterResource(cfg.url),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.padding(top = max(imageOffsetY.value, 0.dp)).alpha(alpha.value)
                .rotate(angle.value).scale(imageScale.value).clip(RoundedCornerShape(16.dp)).shadow(
                    elevation = 24.dp,
                    spotColor = ColorAssets.SurfaceShadow.value,
                    shape = RoundedCornerShape(16.dp)
                ).size(imageWith.value, imageHeight.value)
        )
    }
}