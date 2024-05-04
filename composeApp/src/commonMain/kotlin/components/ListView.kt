package composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import data.SpecificConfiguration

data class ListStyle(
    val cornerSize: Dp,
    val foreground: Color,
    val background: Color,
    val shadow: Shadow
) {
    companion object {
        private val ColorAssets.DefaultListViewShadow: ColorSet
            get() = ColorSet(Color(0xFFEEEEEE), SurfaceShadow.dark)

        val defaultListStyle: ListStyle
            @Composable
            get() = ListStyle(
                cornerSize = 12.dp,
                foreground = ColorAssets.ForegroundColor.value,
                background = ColorAssets.Surface.value,
                shadow = Shadow(
                    color = ColorAssets.DefaultListViewShadow.value,
                    offset = Offset(0f, 6f),
                    blurRadius = 8f
                )
            )
    }
}

@Composable
fun ListView(
    style: ListStyle = ListStyle.defaultListStyle,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val screenSize = SpecificConfiguration.localScreenConfiguration.bounds

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .shadow(
                elevation = style.shadow.blurRadius.dp,
                spotColor = style.shadow.color,
                shape = RoundedCornerShape(style.cornerSize)
            )
            .widthIn(max = screenSize.width * 0.92f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(style.cornerSize))
            .background(style.background)
    ) {
        content()
    }
}