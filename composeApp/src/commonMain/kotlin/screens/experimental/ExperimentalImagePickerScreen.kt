package screens.experimental

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import components.LargeButton
import components.NavigationHeader
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import data.Zero
import data.models.SnapAlertData
import kotlinx.coroutines.launch
import viewmodel.SnapAlertViewModel

object ExperimentalImagePickerScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // Layout inneed:
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight
        val screenSize = SpecificConfiguration.localScreenConfiguration.bounds

        // Camera inneed:
        val sheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        fun sheetCloseHandle() {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) showBottomSheet = false
            }
        }

        val state = rememberPeekabooCameraState(onCapture = { /* Handle captured images */ })

        // File picker inneed:
        var showFilePicker by remember { mutableStateOf(false) }
        val fileType = listOf("jpg", "png")

        // Image picker inneed:
        val singleImagePicker = rememberImagePickerLauncher(
            selectionMode = SelectionMode.Single,
            scope = scope,
            onResult = { byteArrays ->
                byteArrays.firstOrNull()?.let {
                    // Process the selected images' ByteArrays.
                    println(it)
                }
            }
        )

        // Component view:
        Surface {
            NavigationHeader("Experimental Picker")

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(top = topOffset)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LargeButton("Pick single file", modifier = Modifier.fillMaxWidth()) {
                    showFilePicker = true
                }

                LargeButton("Pick Single Image", modifier = Modifier.fillMaxWidth()) {
                    singleImagePicker.launch()
                }

                LargeButton("Open camera", modifier = Modifier.fillMaxWidth()) {
                    showBottomSheet = true
                }
            }
        }

        FilePicker(show = showFilePicker, fileExtensions = fileType) { platformFile ->
            showFilePicker = false
            // do something with the file
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState,
                containerColor = Color.Black,
                windowInsets = WindowInsets.Zero
            ) {
                PeekabooCamera(
                    state = state,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f),
                    permissionDeniedContent = {
                        sheetCloseHandle()
                        SnapAlertViewModel.pushSnapAlert(SnapAlertData("Camera permission denied."))
                    },
                )
            }
        }
    }
}
