package components.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.preat.peekaboo.image.picker.toImageBitmap
import components.ColorAssets
import components.properties
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Checkmark
import compose.icons.evaicons.outline.Close
import data.SpecificConfiguration

data class ImageFilePreviewScreen(
    private val image: ByteArray,
    private val bindingImageUrl: MutableState<ByteArray?>,
    private val sheetCloseHandle: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var showOperation by remember { mutableStateOf(true) }
        val navOffsetY = animateDpAsState(
            targetValue = if (showOperation) 0.dp else SpecificConfiguration.localScreenConfiguration.bounds.height,
            animationSpec = tween(MaterialTheme.properties.defaultAnimationDuration.toInt())
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(Modifier.fillMaxSize().weight(1f).background(Color.Black)) {
                Image(
                    bitmap = image.toImageBitmap(),
                    contentDescription = "Selected Image",
                    modifier = Modifier.clickable { showOperation = !showOperation }.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                modifier = Modifier.offset(y = navOffsetY.value).fillMaxWidth()
                    .background(ColorAssets.Background.dark).navigationBarsPadding()
                    .padding(SpecificConfiguration.defaultContentPadding).padding(bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(EvaIcons.Outline.Close, null, Modifier.clickable {
                    navigator.pop()
                }.size(24.dp), Color.White)

                Icon(EvaIcons.Outline.Checkmark, null, Modifier.clickable {
                    bindingImageUrl.value = image
                    sheetCloseHandle()
                }.size(24.dp), Color.White)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ImageFilePreviewScreen

        if (!image.contentEquals(other.image)) return false
        if (bindingImageUrl != other.bindingImageUrl) return false
        if (sheetCloseHandle != other.sheetCloseHandle) return false

        return true
    }

    override fun hashCode(): Int {
        var result = image.contentHashCode()
        result = 31 * result + bindingImageUrl.hashCode()
        result = 31 * result + sheetCloseHandle.hashCode()
        return result
    }
}