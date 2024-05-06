package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val SurfaceColors.Companion.defaultListItemColors: SurfaceColors
    @Composable
    get() = SurfaceColors(
        foreground = MaterialTheme.colors.onSurface,
        surface = MaterialTheme.colors.surface,
        background = MaterialTheme.colors.surface
    )

@Composable
fun ListItem(
    icon: @Composable () -> Unit,
    title: String,
    colors: SurfaceColors = SurfaceColors.defaultListItemColors,
    trailing: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Surface {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
                .background(colors.background.value)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                icon()

                Spacer(Modifier.width(8.dp))

                Text(title, fontSize = 15.sp)
            }

            if (trailing != null) trailing() else Spacer(Modifier.width(1.dp))
        }
    }
}


class ListIconSize(
    val rect: Dp,
    val padding: Dp
) {
    companion object {
        val TINY = ListIconSize(18.dp, 4.dp)
        val MEDIUM = ListIconSize(26.dp, 6.dp)
        val LARGE = ListIconSize(34.dp, 8.dp)
    }
}

@Composable
fun IconListItem(
    icon: ImageVector,
    title: String,
    color: Color,
    size: ListIconSize = ListIconSize.LARGE,
    trailing: (@Composable () -> Unit)? = {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = "Arrow_right_icon",
            tint = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
        )
    },
    onClick: () -> Unit
) {
    @Composable
    fun ConsiderIcon() {
        Icon(
            imageVector = icon,
            contentDescription = title.replace("", "_").lowercase(),
            tint = color.contrastColor(),
            modifier = Modifier
                .size(size.rect).clip(CircleShape)
                .background(color).padding(size.padding)
        )
    }

    ListItem(
        icon = { ConsiderIcon() },
        title = title,
        trailing = trailing
    ) { onClick() }
}