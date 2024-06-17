package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import data.SpecificConfiguration

@Composable
fun ListItem(
    imageVector: ImageVector,
    tint: Color,
    title: String,
    sub: String? = null,
    trailChild: @Composable () -> Unit = {
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier, ColorAssets.Gray.value)
    },
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClick() }.fillMaxWidth()
            .padding(SpecificConfiguration.defaultContentPadding, 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(Modifier.weight(1f), Arrangement.spacedBy(12.dp), Alignment.CenterVertically) {
            Row(
                modifier = Modifier.size(50.dp).clip(CircleShape)
                    .background(tint.copy(alpha = 0.1f)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) { Icon(imageVector, null, Modifier.size(22.dp), tint) }

            Column(Modifier.weight(1f), Arrangement.spacedBy(0.dp)) {
                Text(title)
                sub?.let {
                    Text(
                        text = it,
                        color = ColorAssets.DeepGray.value,
                        style = MaterialTheme.typography.caption,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        //trail child
        trailChild()
    }
}