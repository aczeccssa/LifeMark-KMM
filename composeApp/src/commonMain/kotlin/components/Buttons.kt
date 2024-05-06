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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val SurfaceColors.Companion.defaultButtonColors: SurfaceColors
    @Composable
    get() = SurfaceColors(
        foreground = MaterialTheme.colors.onPrimary,
        surface = MaterialTheme.colors.surface,
        background = MaterialTheme.colors.primary
    )

@Composable
fun LargeButton(
    text: String,
    colors: SurfaceColors = SurfaceColors.defaultButtonColors,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(9999.dp))
            .background(colors.background.value)
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onPrimary
        )
    }
}