package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SnapAlert(
    message: String,
    foreground: Color = MaterialTheme.colors.onSurface,
    background: Color = MaterialTheme.colors.surface,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                spotColor = ColorAssets.SurfaceShadow.value,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(background)
            .fillMaxWidth()
            .padding(12.dp, 4.dp)
    ) {
        Text(message, color = foreground, fontSize = 13.sp)

        trailing()
    }
}

@Composable
@Preview
fun SnapAlert_Preview() {
    SnapAlert("Loading serialization...") {
        Button({ }) { Text("Skip") }
    }
}