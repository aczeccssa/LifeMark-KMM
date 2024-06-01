package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val SurfaceColors.Companion.defaultButtonColors: SurfaceColors
    @Composable get() = SurfaceColors(
        foreground = MaterialTheme.colors.onPrimary,
        surface = MaterialTheme.colors.surface,
        background = MaterialTheme.colors.primary
    )

val SurfaceColors.Companion.secondaryButtonColors: SurfaceColors
    @Composable get() = SurfaceColors.defaultButtonColors.apply {
        surface = ColorSet(Color.LightGray)
        foreground = ColorSet(Color.Gray)
    }

@Composable
fun LargeButton(
    text: String,
    colors: SurfaceColors = SurfaceColors.defaultButtonColors,
    clip: Shape = RoundedCornerShape(100),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(modifier.clip(clip).background(colors.background.value).clickable { onClick() }
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