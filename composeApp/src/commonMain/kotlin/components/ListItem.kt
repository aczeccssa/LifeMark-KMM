package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import data.SpecificConfiguration

@Composable
fun ListItem(
    imageVector: ImageVector, tint: Color, text: String, trailChild: @Composable () -> Unit = {
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier, ColorAssets.Gray.value)
    }, onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClick() }.fillMaxWidth()
            .padding(SpecificConfiguration.defaultContentPadding, 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            Modifier.size(46.dp).clip(CircleShape).background(tint.copy(alpha = 0.1f)),
            Arrangement.Center,
            Alignment.CenterVertically,
        ) { Icon(imageVector, null, Modifier.size(22.dp), tint) }
        Text(text)
        Spacer(Modifier.weight(1f))
        trailChild()
    }
}