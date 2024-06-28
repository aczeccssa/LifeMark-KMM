package screens.experimental

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import com.preat.peekaboo.image.picker.toImageBitmap
import components.CameraView
import components.ColorAssets
import components.ColumnRoundedContainer
import components.LargeButton
import components.NavigationHeader
import components.SurfaceColors
import components.screens.ImageFilePreviewScreen
import components.secondaryButtonColors
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import data.Zero
import data.platform.LocalPreferences
import data.platform.toDataUrl
import data.units.now
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import screens.NAVIGATION_BAR_HEIGHT
import viewmodel.SnapAlertViewModel

object ExperimentalImagePickerScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        // Layout need:
        val viewModel = viewModel { ExperimentalImagePickerViewModel() }
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight
        val currentByteArray: MutableState<ByteArray?> = remember { viewModel.savedByteArray }
        var parsable by remember { mutableStateOf(false) }
        val previewSize = min(
            a = SpecificConfiguration.localScreenConfiguration.bounds.width - (SpecificConfiguration.defaultContentPadding * 2),
            b = 520.dp
        )

        LaunchedEffect(currentByteArray.value) {
            currentByteArray.value?.also { viewModel.saveImage(it) }
            if (currentByteArray.value === null) parsable = false
        }

        // Camera need:
        val sheetState = rememberModalBottomSheetState(true)
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        fun sheetCloseHandle() {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) showBottomSheet = false
            }
        }

        // Component view:
        Surface {
            NavigationHeader(
                title = "Experimental Picker",
                configuration = NavigationHeaderConfiguration.transparentConfiguration
            )

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding)
                    .padding(top = topOffset, bottom = NAVIGATION_BAR_HEIGHT),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(currentByteArray.value !== null) {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(previewSize)
                            .background(ColorAssets.LightGray.value),
                        contentAlignment = Alignment.Center
                    ) {
                        currentByteArray.value?.also {
                            Image(
                                bitmap = it.toImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                LargeButton("Open camera", RoundedCornerShape(12.dp)) {
                    showBottomSheet = true
                }

                currentByteArray.value?.also {
                    LargeButton(
                        text = "Clean cache",
                        clip = RoundedCornerShape(12.dp),
                        colors = SurfaceColors.secondaryButtonColors
                    ) { viewModel.clearCache() }

                    LargeButton(
                        text = "Parse DataUrl",
                        clip = RoundedCornerShape(12.dp),
                        colors = SurfaceColors.secondaryButtonColors
                    ) { parsable = true }

                    AnimatedVisibility(parsable) { expDataUrlParser(it) }
                }
            }
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
                Navigator(CameraCaptureScreen(currentByteArray) { sheetCloseHandle() }) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }
}

class ExperimentalImagePickerViewModel(val id: Uuid = uuid4()) : ViewModel() {
    private val _savedByteArray: MutableState<ByteArray?> = mutableStateOf(null)
    val savedByteArray: MutableState<ByteArray?> get() = _savedByteArray

    companion object {
        private const val IMAGE_PICKER_BYTE_KEY = "experimental_image_picker_bitmap_key"
        private const val NULL_BYTE_PLACEHOLDER = "null"
        private const val TAG = "ExperimentalImagePicker"
    }

    init {
        updateSavedImage()
    }

    fun saveImage(byteArray: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            LocalPreferences.putString(IMAGE_PICKER_BYTE_KEY, Json.encodeToString(byteArray))
            Napier.i("${LocalDateTime.now()} - Success to save selected image as cache in local defaults.", tag = TAG)
        }
    }

    private fun updateSavedImage() {
        viewModelScope.launch(Dispatchers.IO) { // Using IO threads
            val data = LocalPreferences.getString(IMAGE_PICKER_BYTE_KEY, NULL_BYTE_PLACEHOLDER)
            if (data == NULL_BYTE_PLACEHOLDER) {
                Napier.w("${LocalDateTime.now()} - Nothing get from local defaults.", tag = TAG)
            } else {
                val result = Json.decodeFromString<ByteArray>(data)
                Napier.i("${LocalDateTime.now()} - Success get cache from local defaults: $result.", tag = TAG)
                _savedByteArray.value = result
            }
        }
    }

    fun clearCache() {
        LocalPreferences.remove(IMAGE_PICKER_BYTE_KEY)
        Napier.i("${LocalDateTime.now()} - Saved selected image cache already cleaned.", tag = TAG)
        _savedByteArray.value = null
    }
}

private data class CameraCaptureScreen(
    val bindingByteArray: MutableState<ByteArray?>, val sheetCloseHandle: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        fun pushNext(bytes: ByteArray) {
            navigator.push(ImageFilePreviewScreen(bytes, bindingByteArray, sheetCloseHandle))
        }

        CameraView(onCaptured = { pushNext(it) }, permissionDeniedContent = {
            sheetCloseHandle()
            SnapAlertViewModel.pushSnapAlert("Camera permission denied.")
        }, topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(SpecificConfiguration.defaultContentPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(Modifier.fillMaxWidth().weight(1f))

                Icon(
                    imageVector = EvaIcons.Outline.Close,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clickable { sheetCloseHandle() }.size(24.dp)
                )
            }
        })
    }
}


private const val PROCESS_FAILED_VALUE = "Failed to parse data url."

@Composable
fun expDataUrlParser(byte: ByteArray) {
    var parsed by remember { mutableStateOf(PROCESS_FAILED_VALUE) }
    var parsing by remember { mutableStateOf(true) }
    var color by remember { mutableStateOf(ColorAssets.Red) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        parsing = true
        scope.launch {
            parsed = try {
                byte.toDataUrl() ?: PROCESS_FAILED_VALUE
            } catch (e: Exception) {
                e.message ?: PROCESS_FAILED_VALUE
            }
        }
        color = if (parsed === PROCESS_FAILED_VALUE) ColorAssets.Red else ColorAssets.Green
        parsing = false
    }

    ColumnRoundedContainer(cornerSize = 12.dp) {
        if (parsing) {
            Box { CircularProgressIndicator() }
        } else {
            Text(parsed, color = color.value, style = MaterialTheme.typography.caption)
        }
    }
}