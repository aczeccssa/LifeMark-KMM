package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.preat.peekaboo.image.picker.ImagePickerLauncher
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.PeekabooCameraState
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Image
import compose.icons.evaicons.outline.Refresh
import data.SpecificConfiguration

@Composable
fun CameraView(
    onCaptured: (ByteArray) -> Unit,
    permissionDeniedContent: () -> Unit,
    topBar: @Composable () -> Unit = { },
    bottomBar: @Composable (PeekabooCameraState, ImagePickerLauncher) -> Unit = { cameraState, singleImagePicker ->
        CameraController(cameraState, singleImagePicker)
    }
) {
    val scope = rememberCoroutineScope()

    val cameraState = rememberPeekabooCameraState(onCapture = {
        it?.also { onCaptured(it) }
    })
    val singleImagePicker = rememberImagePickerLauncher(SelectionMode.Single, scope) { it ->
        it.firstOrNull()?.also { onCaptured(it) }
    }

    Column(verticalArrangement = Arrangement.SpaceBetween) {
        topBar()

        PeekabooCamera(
            state = cameraState,
            modifier = Modifier.fillMaxWidth().weight(1f),
            permissionDeniedContent = {
                permissionDeniedContent()
            },
        )

        bottomBar(cameraState, singleImagePicker)
    }
}

@Composable
fun CameraController(state: PeekabooCameraState, imagePickerState: ImagePickerLauncher? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().navigationBarsPadding()
            .padding(SpecificConfiguration.defaultContentPadding).padding(bottom = 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = EvaIcons.Outline.Image,
            contentDescription = null,
            tint = if (imagePickerState === null) Color.Transparent else Color.White,
            modifier = Modifier.clickable(enabled = imagePickerState !== null, onClick = {
                if (!state.isCapturing) imagePickerState?.launch()
            }).size(24.dp)
        )

        Rectangle(
            size = DpSize(48.dp, 48.dp),
            modifier = Modifier.clickable(enabled = !state.isCapturing, onClick = {
                if (state.isCameraReady) state.capture()
            }).clip(CircleShape).background(Color.White)
        )

        Icon(
            imageVector = EvaIcons.Outline.Refresh,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.clickable {
                if (!state.isCapturing && state.isCameraReady) {
                    state.toggleCamera()
                }
            }.size(24.dp)
        )
    }
}