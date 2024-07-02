package screens.experimental

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import components.AlertLargeButton
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
import compose.icons.evaicons.fill.EyeOff
import compose.icons.evaicons.outline.Trash
import compose.icons.evaicons.outline.Trash2
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import utils.MediaLinkCache
import kotlin.random.Random

object ExperimentalImageSetScreen : Screen {
    private const val THRESHOLD = 5

    @Composable
    override fun Content() {
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight
        val imageList: MutableList<Url> = remember { mutableStateListOf() }
        val imageSize = SpecificConfiguration.localScreenConfiguration.bounds.width * 0.9f
        val showPicturePreview = remember { mutableStateOf(false) }
        val messageState = rememberMessageState(title = "Alert",
            message = "Are you sure to clean this image list?!",
            acceptHandle = AcceptHandle("Clean") { imageList.clear() },
            cancelHandle = MessageHandle("Cancel") { })

        Surface {
            NavigationHeader("Peekaboo Picker", NavigationHeaderConfiguration.clearConfiguration) {
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
            }

            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding)
                    .padding(top = topOffset, bottom = SpecificConfiguration.defaultContentPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier.padding(top = 12.dp).clickable(
                        enabled = imageList.isNotEmpty(),
                        onClick = { showPicturePreview.value = true },
                        indication = null,
                        interactionSource = MutableInteractionSource()
                    )
                ) {
                    if (imageList.isEmpty()) {
                        Icon(
                            imageVector = EvaIcons.Fill.EyeOff,
                            contentDescription = null,
                            tint = ColorAssets.LMPurple.value.copy(alpha = 0.1f),
                            modifier = Modifier.size(200.dp)
                        )
                    } else {
                        imageList.filterIndexed { i, _ -> i < THRESHOLD - 1 }.forEach { image ->
                            KamelImage(
                                resource = asyncPainterResource(image),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.rotate(randomAngle()).scale(0.7f)
                                    .clip(RoundedCornerShape(16.dp)).shadow(
                                        elevation = 24.dp,
                                        spotColor = ColorAssets.SurfaceShadow.value,
                                        shape = RoundedCornerShape(16.dp)
                                    ).size(imageSize)
                            )
                        }
                    }
                }

                Spacer(Modifier.fillMaxHeight().weight(1f))

                SecondaryLargeButton("Add Picture", RoundedCornerShape(12.dp)) {
                    imageList.add(MediaLinkCache.randomImage())
                }
            }

            Message(messageState)
        }

        SourceMutableImagePreview(
            state = showPicturePreview,
            list = imageList,
            sourceSize = Size(imageSize.value, imageSize.value),
            sourceOffset = Offset(
                SpecificConfiguration.defaultContentPadding.value, topOffset.value
            )
        )
    }

    private val anglePool = mutableListOf<Float>()

    private fun randomAngle(min: Float = 0f, max: Float = 36f): Float {
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
}