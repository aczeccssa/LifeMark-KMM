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

private val baseModifier
    @Composable get() = Modifier
        .shadow(8.dp, RoundedCornerShape(24.dp), spotColor = ColorAssets.ThisShadow.value.copy(alpha = 0.01f))
        .fillMaxWidth()

data class ContainerSize(val max: Dp) {
    companion object {
        val UNLIMITED = ContainerSize(max = Int.MAX_VALUE.dp)
    }

    internal val modifier: Modifier
        @Composable get() = if (this === UNLIMITED) baseModifier else baseModifier.heightIn(max = max)
}

@Composable
fun ColumnRoundedContainer(
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    background: ColorSet = ColorAssets.Surface,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = baseModifier.clip(RoundedCornerShape(12.dp))
            .background(background.value, shape = RoundedCornerShape(16.dp)).padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun RowRoundedContainer(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    background: ColorSet = ColorAssets.Surface,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = baseModifier.clip(RoundedCornerShape(12.dp))
            .background(background.value, shape = RoundedCornerShape(16.dp)).padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun RoundedContainer(
    size: ContainerSize = ContainerSize.UNLIMITED,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    background: ColorSet = ColorAssets.Surface,
    content: @Composable() (BoxScope.() -> Unit)
) {
    Box(
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints,
        modifier = size.modifier.clip(RoundedCornerShape(12.dp))
            .background(background.value, shape = RoundedCornerShape(16.dp)).padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun LazyColumnRoundedContainer(
    size: ContainerSize = ContainerSize.UNLIMITED,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    background: ColorSet = ColorAssets.Surface,
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    RoundedContainer(size, Alignment.Center, background = background) {
        LazyColumn(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            userScrollEnabled = userScrollEnabled,
        ) {
            content()
        }
    }
}

@Composable
fun LazyRowRoundedContainer(
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    background: ColorSet = ColorAssets.Surface,
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    RoundedContainer(ContainerSize.UNLIMITED, Alignment.Center, background = background) {
        LazyRow(
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            userScrollEnabled = userScrollEnabled,
        ) {
            content()
        }
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
                "VIEWMORE...",
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