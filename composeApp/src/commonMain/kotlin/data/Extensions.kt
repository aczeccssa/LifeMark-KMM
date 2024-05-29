package data

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

fun Boolean.toggle(): Boolean {
    return !this
}

fun String.toInt(default: Int): Int {
    return try {
        this.toInt()
    } catch (exception: Exception) {
        println(exception)
        default
    } catch (error: Error) {
        println(error)
        default
    }
}

val WindowInsets.Companion.Unify: WindowInsets
    @Composable get() = WindowInsets(0.dp, 0.dp, 0.dp, SpecificConfiguration.edgeSafeArea.asPaddingValues().calculateBottomPadding())

val WindowInsets.Companion.Zero: WindowInsets
    get() = WindowInsets(0, 0, 0, 0)