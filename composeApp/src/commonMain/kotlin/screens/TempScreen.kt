package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TempView(name: String, icon: ImageVector? = null) {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
        ) {
            // Temp icon
            icon?.let { Icon(icon, name, Modifier.size(64.dp), MaterialTheme.colors.primary) }
            // Separate these contents
            Spacer(Modifier.height(8.dp))
            // Temp name
            Text(name, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
@Preview
private fun TEMP_VIEW_PREVIEW() {
    TempView("Preview", Icons.Rounded.Share)
}