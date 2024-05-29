package components.snapalert

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.ColorAssets
import data.SpecificConfiguration
import data.models.SnapAlertData

@Composable
fun SnapAlert(
    data: SnapAlertData,
    destroyHandler: () -> Unit = { }
) {
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds
    val horizontalSafePadding = SpecificConfiguration.edgeSafeArea.asPaddingValues()
        .calculateLeftPadding(LayoutDirection.Ltr)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(bottom = 8.dp).shadow(
            elevation = 18.dp,
            RoundedCornerShape(32.dp),
            spotColor = ColorAssets.SurfaceShadow.value
        ).width(screenSize.width - horizontalSafePadding * 2).clip(RoundedCornerShape(32.dp))
            .background(data.surface.background.value).padding(12.dp)
    ) {
        Text(
            data.message,
            color = data.surface.foreground.value,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.size(24.dp).clickable { destroyHandler() }.clip(CircleShape)
                .background(Color.LightGray)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Color.Gray
            )
        }
    }
}