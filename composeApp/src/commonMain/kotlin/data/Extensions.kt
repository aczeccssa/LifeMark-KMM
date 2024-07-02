package data

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import data.units.now
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun String.toInt(default: Int): Int {
    return try {
        this.toInt()
    } catch (e: Throwable) {
        Napier.e("Error: failed to convert string to int.", e)
        default
    }
}

val WindowInsets.Companion.Zero: WindowInsets get() = WindowInsets(0, 0, 0, 0)

/**
 * A struct that contains the state of the progress dispatch.
 * @property isLoading [Boolean] Whether the progress is currently loading
 * @property data [T] The data that is currently being loaded
 */
interface ProgressDispatchStateStruct<T> {
    val isLoading: Boolean
    val data: T?
}

fun Offset.roundToIntOffset(): IntOffset = IntOffset(this.x.roundToInt(), this.y.roundToInt())

@Composable
fun Modifier.dragOffsetHandler(
    threshold: Float = SpecificConfiguration.localScreenConfiguration.bounds.height.value,
    onDismiss: () -> Unit
): Modifier {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val animatedOffset = animateOffsetAsState(
        Offset(offsetX, offsetY), spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
    )

    return this.offset { animatedOffset.value.roundToIntOffset() }.pointerInput(Unit) {
        detectDragGestures(onDragEnd = {
            if (offsetX > threshold || offsetY > threshold) onDismiss()
            offsetX = 0f
            offsetY = 0f
        }) { change, _ ->
            offsetX += change.position.x - change.previousPosition.x
            offsetY += change.position.y - change.previousPosition.y
        }
    }
}