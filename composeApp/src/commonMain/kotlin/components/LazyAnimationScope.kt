package components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

/**
 * TODO: LazyAnimationColumn(Provider)
 *   entryAnimate [AnimationSpec<Float>] nullable
 *   exitAnimate [AnimationSpec<Float>] nullable
 *   scope [@Composable LazyListScope.() -> Unit] notnull
 */
@Composable
fun LazyAnimationColumn(
    entryAnimate: EntryAnimationDefinition = defaultEntryDefinition,
    exitAnimate: ExitAnimationDefinition = defaultExitDefinition,
    scope: @Composable LazyAnimationScope.() -> Unit
) {

}

private val defaultEntryDefinition: EntryAnimationDefinition
    get() = { element: @Composable () -> Unit ->
        var launch by remember { mutableStateOf(false) }
        val offsetY = animateDpAsState(if (launch) 0.dp else 24.dp, tween(300))
        val alpha = animateFloatAsState(if (launch) 1f else 0f, tween(300))

        LaunchedEffect(Unit) {
            launch = true
        }

        Box(Modifier.offset(y = offsetY.value).alpha(alpha.value)) { element() }
    }

private val defaultExitDefinition: ExitAnimationDefinition
    get() = { element: @Composable () -> Unit, disposed: Boolean ->
        val offsetY = animateDpAsState(if (disposed) 24.dp else 0.dp, tween(300))
        val alpha = animateFloatAsState(if (disposed) 0f else 1f, tween(300))

        Box(Modifier.offset(y = offsetY.value).alpha(alpha.value)) { element() }
    }


typealias EntryAnimationDefinition = @Composable LazyAnimationScope.(@Composable () -> Unit) -> Unit

typealias ExitAnimationDefinition = @Composable LazyAnimationScope.(@Composable () -> Unit, Boolean) -> Unit

// Scope based like [LazyListScope]
class LazyAnimationScope