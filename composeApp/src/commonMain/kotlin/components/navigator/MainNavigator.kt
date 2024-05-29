package components.navigator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.ColorAssets
import components.ColorSet
import components.SurfaceColors

@Composable
fun MainNavigator(
    icon: ImageVector,
    title: String,
    color: SurfaceColors = SurfaceColors.defaultNavigatorColors,
    trailing: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth().background(color.background.value).padding(16.dp, 6.dp)
            .statusBarsPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color.primary.value,
                modifier = Modifier.size(36.dp)
            )

            Text(title, style = MaterialTheme.typography.h5, fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.weight(1f))

            trailing?.also { it() }
        }
    }
}

val SurfaceColors.primary: ColorSet
    @Composable get() = ColorAssets.LMPurple