package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.preat.peekaboo.image.picker.ImagePickerLauncher
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.PeekabooCameraState
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState

@Composable
fun CameraView(
    onCaptured: (ByteArray) -> Unit,
    permissionDeniedContent: () -> Unit,
    header: @Composable () -> Unit = { },
    footer: @Composable (PeekabooCameraState, ImagePickerLauncher) -> Unit = { cameraState, singleImagePicker ->
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
        header()

        PeekabooCamera(
            state = cameraState,
            modifier = Modifier.fillMaxWidth().weight(1f),
            permissionDeniedContent = {
                permissionDeniedContent()
            },
        )

        footer(cameraState, singleImagePicker)
    }
}