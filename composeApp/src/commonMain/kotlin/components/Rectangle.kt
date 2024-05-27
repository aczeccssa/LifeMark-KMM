package components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize

@Composable
fun Rectangle(
    size: DpSize,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = { }
) {
    Box(modifier.size(size)) { content() }
}