package components.navigator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import components.SurfaceColors

@Composable
fun MainNavigator(
    title: String,
    color: SurfaceColors = SurfaceColors.defaultNavigatorColors,
    trailing: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth().background(color.background.value).padding(16.dp, 6.dp)
            .statusBarsPadding()
    ) {
        Row(Modifier.height(42.dp), Arrangement.Center, Alignment.CenterVertically) {
            Text(title, style = MaterialTheme.typography.h5, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.weight(1f))
            // MARK: Optional: Trailing component
            trailing?.also { it() }
        }
    }
}