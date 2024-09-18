package components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.preat.peekaboo.image.picker.toImageBitmap
import data.SpecificConfiguration
import data.dragOffsetHandler
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlinx.coroutines.delay
import screens.NAVIGATION_BAR_HEIGHT
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@Composable
fun SourceSinglePreview(
    state: MutableState<Boolean>,
    sourceSize: Size,
    sourceOffset: Offset,
    enableDragAlpha: Boolean = true,
    content: @Composable (State<Size>, State<Dp>, State<Float>, State<Offset>, MutableState<Float>) -> Unit
) {
    var showPictureView by remember { mutableStateOf(false) }
    val duration = 300
    val pictureSize = animateSizeAsState(
        if (state.value) Size(
            SpecificConfiguration.localScreenConfiguration.bounds.width.value,
            SpecificConfiguration.localScreenConfiguration.bounds.height.value + NAVIGATION_BAR_HEIGHT.value
        ) else Size(sourceSize.width, sourceSize.height), tween(duration)
    )
    val pictureRounded = animateDpAsState(if (state.value) 0.dp else 16.dp, tween(duration))
    val offsetBackgroundAlpha = remember { mutableStateOf(1f) }
    val animatedBackgroundAlpha = animateFloatAsState(
        if (enableDragAlpha) offsetBackgroundAlpha.value else 1f, tween(duration)
    )
    val pictureAlpha =
        animateFloatAsState(if (state.value) animatedBackgroundAlpha.value else 0f, tween(duration))
    val pictureOffset =
        animateOffsetAsState(if (state.value) Offset(0f, 0f) else sourceOffset, tween(duration))

    LaunchedEffect(state.value) {
        if (!state.value) delay(duration.toLong())
        showPictureView = state.value
    }

    if (showPictureView) content(
        pictureSize, pictureRounded, pictureAlpha, pictureOffset, offsetBackgroundAlpha
    )
}

@Composable
fun SourceSingleImagePreview(
    state: MutableState<Boolean>,
    image: MutableState<ByteArray?>,
    sourceSize: Size,
    sourceOffset: Offset,
    enableDragAlpha: Boolean = true,
) {
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds
    SourceSinglePreview(
        state, sourceSize, sourceOffset, enableDragAlpha
    ) { pictureSize, pictureRounded, pictureAlpha, pictureOffset, bgAlpha ->
        image.value?.also {
            Box(Modifier.offset(pictureOffset.value.x.dp, pictureOffset.value.y.dp).zIndex(10f)
                .alpha(pictureAlpha.value).clickable { state.value = false }
                .size(pictureSize.value.width.dp, pictureSize.value.height.dp)
                .background(Color.Black).clip(RoundedCornerShape(pictureRounded.value)),
                Alignment.Center) {
                Image(bitmap = it.toImageBitmap(),
                    contentDescription = "Preview image.",
                    modifier = Modifier.dragOffsetHandler(onChange = { offset ->
                        val diff = -offset.y.absoluteValue / screenSize.height.value
                        bgAlpha.value = min(max(diff, 0.2f), 1f)
                    }, onCancel = {
                        bgAlpha.value = 1f
                    }) { state.value = false }.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun SourceSingleImagePreview(
    state: MutableState<Boolean>,
    image: Url,
    sourceSize: Size,
    sourceOffset: Offset,
    enableDragAlpha: Boolean = true,
) {
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds

    SourceSinglePreview(
        state, sourceSize, sourceOffset, enableDragAlpha
    ) { pictureSize, pictureRounded, pictureAlpha, pictureOffset, bgAlpha ->
        Box(Modifier.offset(pictureOffset.value.x.dp, pictureOffset.value.y.dp).zIndex(10f)
            .alpha(pictureAlpha.value).clickable { state.value = false }
            .size(pictureSize.value.width.dp, pictureSize.value.height.dp).background(Color.Black)
            .clip(RoundedCornerShape(pictureRounded.value)),
            Alignment.Center) {
            KamelImage(resource = asyncPainterResource(image),
                contentDescription = "Preview image",
                modifier = Modifier.dragOffsetHandler(onChange = { offset ->
                    val diff = -offset.y.absoluteValue / screenSize.height.value
                    bgAlpha.value = min(max(diff, 0.2f), 1f)
                }, onCancel = {
                    bgAlpha.value = 1f
                }) { state.value = false }.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SourceMutableImagePreview(
    state: MutableState<Boolean>,
    list: List<Url>,
    sourceSize: Size,
    sourceOffset: Offset,
    showIndex: Boolean = true,
    initIndex: Int = 0,
    enableDragAlpha: Boolean = true
) {
    var showPictureView by remember { mutableStateOf(false) }
    val duration = 300
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds
    val pictureSize = animateSizeAsState(
        if (state.value) Size(
            screenSize.width.value, screenSize.height.value + NAVIGATION_BAR_HEIGHT.value
        ) else Size(sourceSize.width, sourceSize.height), tween(duration)
    )
    val pictureRounded = animateDpAsState(if (state.value) 0.dp else 16.dp, tween(duration))

    var offsetBackgroundAlpha by remember { mutableStateOf(1f) }
    val animatedBackgroundAlpha =
        animateFloatAsState(if (enableDragAlpha) offsetBackgroundAlpha else 1f, tween(duration))
    val pictureAlpha =
        animateFloatAsState(if (state.value) animatedBackgroundAlpha.value else 0f, tween(duration))
    val pictureOffset =
        animateOffsetAsState(if (state.value) Offset(0f, 0f) else sourceOffset, tween(duration))

    val pagerState = rememberPagerState(initIndex) { list.size }

    LaunchedEffect(state.value) {
        if (!state.value) delay(duration.toLong())
        showPictureView = state.value
    }

    if (showPictureView) {
        Box(Modifier.offset(pictureOffset.value.x.dp, pictureOffset.value.y.dp).zIndex(10f)
            .alpha(pictureAlpha.value).size(pictureSize.value.width.dp, pictureSize.value.height.dp)
            .background(Color.Black).clip(RoundedCornerShape(pictureRounded.value))
            .dragOffsetHandler(onChange = { offset ->
                val diff = -offset.y.absoluteValue / screenSize.height.value
                offsetBackgroundAlpha = min(max(diff, 0.2f), 1f)
            }, onCancel = {
                offsetBackgroundAlpha = 1f
            }) { state.value = false }, Alignment.Center
        ) {
            LaunchedEffect(Unit) {
                pagerState.scrollToPage(initIndex)
            }

            HorizontalPager(pagerState, Modifier.clickable(enabled = false) { }) { current ->
                list.forEachIndexed { index, url ->
                    if (current == index) {
                        KamelImage(
                            resource = asyncPainterResource(url),
                            contentDescription = "Preview image",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            if (showIndex) {
                Column(Modifier.fillMaxSize(), Arrangement.Bottom, Alignment.CenterHorizontally) {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${list.size}",
                        modifier = Modifier.navigationBarsPadding(),
                        color = Color.White
                    )
                }
            }
        }
    }
}

