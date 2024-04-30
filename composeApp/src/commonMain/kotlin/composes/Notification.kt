package composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import data.SpecificConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Notifiction(
    title: String,
    message: String,
    background: Color = MaterialTheme.colors.surface,
    foreground: Color = MaterialTheme.colors.onSurface,
    trailing: (@Composable () -> Unit)? = null
) {
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds
    val horizontalSafePadding =
        SpecificConfiguration.edgeSafeArea.asPaddingValues().calculateLeftPadding(LayoutDirection.Ltr)
    val containerShpe = RoundedCornerShape(18.dp)
    val trailingLimitSize = 52.dp
    val containerPadding = 12.dp
    val contentOffset = 2.dp
    val contentSpace = 4.dp
    val titleStyle = TextStyle(
        fontSize = 15.sp,
        lineHeight = 15.sp,
        fontWeight = FontWeight.Medium,
        color = foreground
    )
    val messageStyle = TextStyle(
        fontSize = 13.sp,
        lineHeight = 13.sp,
        fontWeight = FontWeight.Normal,
        color = foreground.copy(alpha = 0.8f)
    )

    LaunchedEffect(Unit) {
        println("Horizontal safe edge: $horizontalSafePadding")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.zIndex(4f)
            .shadow(elevation = 12.dp, containerShpe, spotColor = ColorAssets.SurfaceShadow.value)
            .fillMaxWidth().widthIn(max = screenSize.width - horizontalSafePadding)
            .clip(containerShpe).background(background).padding(containerPadding)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = contentOffset)
        ) {
            // Title of this notifiction
            Text(title, style = titleStyle, maxLines = 1, overflow = TextOverflow.Ellipsis)

            // Sperator between these component
            Spacer(Modifier.height(contentSpace))

            // Message of this notifiction
            Text(message, style = messageStyle, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

        Spacer(Modifier.width(horizontalSafePadding))

        trailing?.let {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.heightIn(max = trailingLimitSize).widthIn(max = trailingLimitSize)
            ) { it() }
        }
    }
}

@Composable
@Preview
fun NOTICICTION_PREVIEW() {
    // Notifiction()
}