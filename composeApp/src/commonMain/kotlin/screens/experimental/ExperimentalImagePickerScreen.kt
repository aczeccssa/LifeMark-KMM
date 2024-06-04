package screens.experimental

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import components.ColumnRoundedContainer
import components.LargeButton
import components.NavigationHeader
import data.NavigationHeaderConfiguration
import data.Zero
import kotlinx.coroutines.launch

object ExperimentalImagePickerScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        var showFilePicker by remember { mutableStateOf(false) }
        val fileType = listOf("jpg", "png")
        val sheetState = rememberModalBottomSheetState()
        var showBottomSheet by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight
        fun sheetCloseHandle() {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) showBottomSheet = false
            }
        }

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

        val state = rememberPeekabooCameraState(onCapture = { /* Handle captured images */ })

        Surface {
            NavigationHeader("Experimental Picker")

            Column(Modifier.fillMaxSize().verticalScroll(scrollState).padding(top = topOffset)) {
                ColumnRoundedContainer(
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
                    modifier = Modifier.fillMaxSize(),
                    permissionDeniedContent = {
                        // Custom UI content for permission denied scenario
                        sheetCloseHandle()
                    },
                )
            }
        }
    }
}