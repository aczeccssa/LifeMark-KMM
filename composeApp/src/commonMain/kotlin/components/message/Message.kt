package components.message

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import components.ColorAssets
import data.SpecificConfiguration
import kotlinx.coroutines.delay

@Composable
fun Message(state: MessageState, onDismiss: () -> Unit = { }) {
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds
    var launched by remember { mutableStateOf(false) }

    val animationDuration = 300
    val backgroundColorAlpha =
        animateFloatAsState(if (state.launched.value) 0.3f else 0f, tween(animationDuration))
    val contentYOffset = animateDpAsState(
        if (state.launched.value) 0.dp else screenSize.height, tween(animationDuration)
    )

    val containerClipShape = RoundedCornerShape(24.dp)

    LaunchedEffect(state.launched.value) {
        if (!state.launched.value) delay(animationDuration.toLong())
        launched = state.launched.value
    }

    if (launched) {
        Column(
            modifier = Modifier.clickable(
                onClick = { onDismiss() },
                indication = null,
                interactionSource = MutableInteractionSource()
            ).fillMaxSize().background(Color.Black.copy(alpha = backgroundColorAlpha.value)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.clickable(
                    onClick = { }, indication = null, interactionSource = MutableInteractionSource()
                ).offset(y = contentYOffset.value).padding(horizontal = 12.dp).shadow(
                    12.dp, containerClipShape, spotColor = ColorAssets.SurfaceShadow.value
                ).background(ColorAssets.SurfaceVariant.value).clip(containerClipShape)
                    .padding(12.dp), Arrangement.spacedBy(6.dp), Alignment.CenterHorizontally
            ) {
                Text(state.title, style = MaterialTheme.typography.subtitle1)
                Text(state.message)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier.clickable {
                            state.acceptHandle.handle()
                            state.close()
                        }.fillMaxWidth().weight(1f).clip(containerClipShape)
                            .background(ColorAssets.LMPurple.value).padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            state.acceptHandle.text, color = Color.White
                        )
                    }

                    state.cancelHandle?.let {
                        Box(
                            modifier = Modifier.clickable {
                                it.handle()
                                state.close()
                            }.fillMaxWidth().weight(1f).clip(containerClipShape)
                                .background(ColorAssets.LightGray.value).padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) { Text(it.text, color = MaterialTheme.colors.onSurface) }
                    }
                }
            }
        }
    }
}

open class MessageHandle(open val text: String, open val handle: () -> Unit)

class AcceptHandle(override val text: String, override val handle: () -> Unit) :
    MessageHandle(text, handle)

class MessageState private constructor(
    var title: String,
    var message: String,
    var acceptHandle: AcceptHandle,
    var cancelHandle: MessageHandle? = null
) {
    companion object {
        fun new(
            title: String,
            message: String,
            acceptHandle: AcceptHandle,
            cancelHandle: MessageHandle? = null
        ): MessageState {
            return MessageState(title, message, acceptHandle, cancelHandle)
        }
    }

    private val _launched = mutableStateOf(false)
    val launched get() = _launched

    fun launch() {
        _launched.value = true
    }

    fun close() {
        _launched.value = false
    }
}

@Composable
fun rememberMessageState(
    title: String, message: String, acceptHandle: AcceptHandle, cancelHandle: MessageHandle? = null
): MessageState {
    return remember { MessageState.new(title, message, acceptHandle, cancelHandle) }
}