package screens.experimental

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.ui.unit.IntOffset
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
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import utils.MediaLinkCache
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.random.Random

object ExperimentalImageSetScreen : Screen {
    private const val THRESHOLD = 5

    @Composable
    override fun Content() {
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight
        val imageList: MutableList<Pair<Url, Float>> = remember { mutableStateListOf() }
        val imageSize = androidx.compose.ui.unit.min(
            SpecificConfiguration.localScreenConfiguration.bounds.width * 0.9f, 520.dp
        )
        val showPicturePreview = remember { mutableStateOf(false) }
        var picturePreviewInitIndex by remember { mutableStateOf(0) }
        val messageState = rememberMessageState(title = "Alert",
            message = "Are you sure to clean this image list?!",
            acceptHandle = AcceptHandle("Clean") { imageList.clear() },
            cancelHandle = MessageHandle("Cancel") { })
        var setTransform by remember { mutableStateOf(true) }
        val verticalScrollState = rememberScrollState()
        val scope = rememberCoroutineScope()

        val transformSwitchColor = animateColorAsState(
            targetValue = if (setTransform) ColorAssets.Green.value else ColorAssets.SK.FillBlue.value,
            animationSpec = tween(Spring.DampingRatioLowBouncy.toInt())
        )

        var offsetX by remember { mutableStateOf(0f) }
        var offsetY by remember { mutableStateOf(0f) }
        val animatedOffset = animateOffsetAsState(
            Offset(offsetX, offsetY), spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
        )
        val offsetYThreshold =
            SpecificConfiguration.localScreenConfiguration.bounds.width.value * 0.5f

        Surface {
            NavigationHeader("Peekaboo Picker", NavigationHeaderConfiguration.clearConfiguration) {
                Row(Modifier, Arrangement.spacedBy(12.dp)) {
                    AnimatedVisibility(imageList.size > THRESHOLD - 1) {
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

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(verticalScrollState)
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding).padding(
                        top = topOffset, bottom = SpecificConfiguration.defaultContentPadding + 8.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(Modifier.padding(top = 12.dp).clickable(MutableInteractionSource(), null) {
                    if (imageList.isNotEmpty() && setTransform) showPicturePreview.value = true
                }) {
                    if (imageList.isEmpty()) {
                        Icon(
                            if (setTransform) EvaIcons.Fill.Layers else EvaIcons.Fill.List,
                            contentDescription = null,
                            tint = ColorAssets.LMPurple.value.copy(alpha = 0.1f),
                            modifier = Modifier.size(200.dp)
                        )
                    } else {
                        val reversedFiledList = imageList.reversed().filterIndexed { i, _ ->
                            if (setTransform) i < THRESHOLD else true
                        }.reversed()
                        reversedFiledList.forEachIndexed { index, pair ->
                            if (index == max(reversedFiledList.size - 1, 0)) {
                                picturePreviewInitIndex = imageList.indexOf(pair)
                            }
                            BoxingImage(pair,
                                120.dp,
                                imageSize,
                                index,
                                setTransform,
                                12.dp,
                                Modifier.offset {
                                    val diff = (index + 1f) / THRESHOLD
                                    Offset(
                                        animatedOffset.value.x * diff, animatedOffset.value.y * diff
                                    ).roundToIntOffset()
                                }.pointerInput(Unit) {
                                    if (setTransform) {
                                        detectDragGestures(
                                            onDragEnd = {
                                                if (offsetY > offsetYThreshold) setTransform = false
                                                offsetY = 0f
                                                offsetX = 0f
                                            },
                                        ) { change, _ ->
                                            offsetX += change.position.x - change.previousPosition.x
                                            offsetY += change.position.y - change.previousPosition.y
                                        }
                                    }
                                })
                        }
                    }
                }
            }

            Column(
                Modifier.fillMaxSize()
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding)
                    .padding(bottom = SpecificConfiguration.defaultContentPadding + 8.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                SecondaryLargeButton("Add Picture", RoundedCornerShape(12.dp)) {
                    scope.launch {
                        imageList.add(Pair(MediaLinkCache.randomImage(), randomAngle()))
                        verticalScrollState.animateScrollTo(verticalScrollState.maxValue)
                    }
                }
            }

            Message(messageState)
        }

        SourceMutableImagePreview(
            state = showPicturePreview,
            list = imageList.map { it.first },
            sourceSize = Size(imageSize.value, imageSize.value),
            sourceOffset = Offset(
                SpecificConfiguration.defaultContentPadding.value, topOffset.value
            ),
            initIndex = max(min(picturePreviewInitIndex, 0), imageList.size - 1)
        )
    }

    private val anglePool = mutableListOf<Float>()

    private fun randomAngle(min: Float = 0f, max: Float = 30f): Float {
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

    @Composable
    private fun BoxingImage(
        pair: Pair<Url, Float>,
        baseSize: Dp,
        transformSize: Dp,
        index: Int,
        transform: Boolean,
        spacing: Dp,
        modifier: Modifier = Modifier
    ) {
        var launch by remember { mutableStateOf(false) }

        val alpha = animateFloatAsState(
            targetValue = if (launch) 1f else 0f,
            animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
        )
        val angle = animateFloatAsState(
            targetValue = if (transform && !launch) randomAngle(
                270f,
                480f
            ) else if (transform && launch) pair.second else 0f,
            animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
        )
        val imageHeight = animateDpAsState(
            targetValue = if (transform) transformSize else baseSize,
            animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
        )
        val imageWith = animateDpAsState(
            targetValue = if (transform) transformSize else SpecificConfiguration.localScreenConfiguration.bounds.width - 2 * SpecificConfiguration.defaultContentPadding,
            animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
        )
        val imageScale = animateFloatAsState(
            targetValue = if (transform) 0.7f else 1f,
            animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
        )
        val imageOffsetY = animateDpAsState(
            targetValue = if (transform && !launch) SpecificConfiguration.localScreenConfiguration.bounds.height else if (transform && launch) 0.dp else index * (baseSize + spacing),
            animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
        )

        LaunchedEffect(Unit) {
            if (transform) launch = true
        }

        KamelImage(
            resource = asyncPainterResource(pair.first),
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