package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

val SurfaceColors.Companion.defaultButtonColors: SurfaceColors
    @Composable get() = SurfaceColors(
        foreground = MaterialTheme.colors.onPrimary,
        surface = MaterialTheme.colors.primary,
        background = MaterialTheme.colors.background
    )

val SurfaceColors.Companion.secondaryButtonColors: SurfaceColors
    @Composable get() = SurfaceColors.defaultButtonColors.apply {
        surface = ColorSet(ColorAssets.LightGray.value)
        foreground = ColorSet(MaterialTheme.colors.primary)
    }

@Composable
fun LargeButton(
    text: String,
    colors: SurfaceColors = SurfaceColors.defaultButtonColors,
    clip: Shape = RoundedCornerShape(100),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(modifier.clip(clip).background(colors.surface.value).clickable { onClick() }
        .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1,
            color = colors.foreground.value
        )
    }
}