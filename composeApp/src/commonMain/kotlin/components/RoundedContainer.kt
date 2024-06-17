package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun ColumnRoundedContainerBaseModifier(
    cornerSize: Dp = 16.dp,
    background: ColorSet = ColorAssets.Surface,
    limit: ContainerSize = ContainerSize.UNLIMITED
): Modifier {
    return Modifier.shadow(
        elevation = 8.dp,
        spotColor = ColorAssets.ThisShadow.value.copy(alpha = 0.01f),
        shape = RoundedCornerShape(cornerSize)
    ).fillMaxWidth().heightIn(max = limit.max).clip(RoundedCornerShape(cornerSize))
        .background(background.value, shape = RoundedCornerShape(cornerSize)).padding(16.dp)
}

data class ContainerSize(val max: Dp) {
    companion object {
        internal val UNLIMITED = ContainerSize(max = Int.MAX_VALUE.dp)
    }
}

@Composable
fun ColumnRoundedContainer(
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    background: ColorSet = ColorAssets.Surface,
    cornerSize: Dp = 16.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = ColumnRoundedContainerBaseModifier(cornerSize, background)
    ) { content() }
}

@Composable
fun RowRoundedContainer(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    background: ColorSet = ColorAssets.Surface,
    cornerSize: Dp = 16.dp,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = ColumnRoundedContainerBaseModifier(cornerSize, background)
    ) { content() }
}

@Composable
fun RoundedContainer(
    size: ContainerSize = ContainerSize.UNLIMITED,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    background: ColorSet = ColorAssets.Surface,
    cornerSize: Dp = 16.dp,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        modifier = ColumnRoundedContainerBaseModifier(cornerSize, background, size)
    ) { content() }
}

@Composable
fun LazyColumnRoundedContainer(
    size: ContainerSize = ContainerSize.UNLIMITED,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    background: ColorSet = ColorAssets.Surface,
    userScrollEnabled: Boolean = true,
    cornerSize: Dp = 16.dp,
    content: LazyListScope.() -> Unit
) {
    RoundedContainer(size, Alignment.Center, background = background, cornerSize = cornerSize) {
        LazyColumn(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            userScrollEnabled = userScrollEnabled,
        ) { content() }
    }
}

@Composable
fun LazyRowRoundedContainer(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    background: ColorSet = ColorAssets.Surface,
    userScrollEnabled: Boolean = true,
    cornerSize: Dp = 16.dp,
    content: LazyListScope.() -> Unit
) {
    RoundedContainer(
        ContainerSize.UNLIMITED, Alignment.Center, background = background, cornerSize = cornerSize
    ) {
        LazyRow(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            userScrollEnabled = userScrollEnabled,
        ) { content() }
    }
}

@Composable
fun ViewMoreOpacityMusk(surface: Color = MaterialTheme.colors.surface, onClick: () -> Unit) {
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.clickable(
                onClick = { onClick() },
                indication = null,
                interactionSource = MutableInteractionSource()
            ).fillMaxWidth().fillMaxHeight(0.4f).heightIn(min = 100.dp).background(
                Brush.verticalGradient(0.0f to Color.Transparent, 1.0f to surface)
            )
        ) {
            Text(
                "VIEW MORE...",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.subtitle2,
                textDecoration = TextDecoration.Underline
            )
            Icon(
                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}