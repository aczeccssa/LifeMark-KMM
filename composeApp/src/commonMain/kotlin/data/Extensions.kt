package data

import androidx.compose.foundation.layout.WindowInsets
import io.github.aakira.napier.Napier

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