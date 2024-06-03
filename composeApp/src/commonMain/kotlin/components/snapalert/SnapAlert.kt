package components.snapalert

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.ColorAssets
import components.Rectangle
import data.SpecificConfiguration
import data.models.SnapAlertData

@Composable
fun SnapAlert(
    data: SnapAlertData, destroyHandler: () -> Unit = { }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.shadow(
            elevation = 18.dp,
            RoundedCornerShape(32.dp),
            spotColor = ColorAssets.SurfaceShadow.value
        ).fillMaxWidth().clip(RoundedCornerShape(32.dp)).background(data.surface.surface.value)
            .padding(16.dp, 12.dp)
    ) {
        Row(Modifier.weight(1f), Arrangement.spacedBy(8.dp), Alignment.CenterVertically) {
            Rectangle(
                DpSize(8.dp, 8.dp),
                Modifier.clip(CircleShape).background(MaterialTheme.colors.primary)
            )

            Text(
                data.message,
                color = data.surface.foreground.value,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = null,
            modifier = Modifier.size(18.dp).clickable { destroyHandler() },
            tint = ColorAssets.DeepGray.value
        )
    }
}