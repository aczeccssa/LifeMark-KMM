package screens.experimental

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import com.darkrockstudios.libraries.mpfilepicker.FilePicker
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import com.preat.peekaboo.image.picker.toImageBitmap
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import components.CameraController
import components.ColorAssets
import components.LargeButton
import components.NavigationHeader
import components.SurfaceColors
import components.properties
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Checkmark
import compose.icons.evaicons.outline.Close
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
        // Layout need:
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight
        val currentBitmap: MutableState<ImageBitmap?> = remember { mutableStateOf(null) }

        // Camera need:
        val sheetState = rememberModalBottomSheetState(true)
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        fun sheetCloseHandle() {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) showBottomSheet = false
            }
        }

        // File picker need:
        var showFilePicker by remember { mutableStateOf(false) }
        val fileType = listOf("jpg", "png")

        // Component view:
        Surface {
            NavigationHeader(
                title = "Experimental Picker",
                configuration = NavigationHeaderConfiguration.defaultConfiguration.copy(
                    color = SurfaceColors.defaultNavigatorColors.copy(
                        surface = ColorAssets.Background
                    )
                )
            )

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                    .background(MaterialTheme.colors.background).padding(top = topOffset)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(260.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ColorAssets.LightGray.value, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    currentBitmap.value?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                LargeButton(
                    text = "Pick single file",
                    clip = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) { showFilePicker = true }

                LargeButton(
                    text = "Open camera",
                    clip = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    showBottomSheet = true
                }
            }
        }

        FilePicker(show = showFilePicker, fileExtensions = fileType) { _ /* platformFile */ ->
            showFilePicker = false // do something with the file
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
                Navigator(CameraCaptureScreen(currentBitmap) { sheetCloseHandle() }) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }
}

private class CameraCaptureScreen(
    val bindingBitmap: MutableState<ImageBitmap?>, val sheetCloseHandle: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()

        fun pushNext(byteArray: ImageBitmap) {
            navigator.push(CameraCapturedPreviewScreen(byteArray, bindingBitmap, sheetCloseHandle))
        }

        val cameraState = rememberPeekabooCameraState(onCapture = {
            it?.also { pushNext(it.toImageBitmap()) }
        })
        val singleImagePicker = rememberImagePickerLauncher(SelectionMode.Single, scope) {
            it.firstOrNull()?.also { pushNext(it.toImageBitmap()) }
        }

        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Row(
                Modifier.fillMaxWidth().padding(SpecificConfiguration.defaultContentPadding),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                Spacer(Modifier.fillMaxWidth().weight(1f))

                Icon(
                    imageVector = EvaIcons.Outline.Close,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        if (!cameraState.isCapturing) sheetCloseHandle()
                    }.size(24.dp)
                )
            }

            PeekabooCamera(
                state = cameraState,
                modifier = Modifier.fillMaxWidth().weight(1f),
                permissionDeniedContent = {
                    sheetCloseHandle()
                    SnapAlertViewModel.pushSnapAlert(SnapAlertData("Camera permission denied."))
                },
            )

            CameraController(cameraState, singleImagePicker)
        }
    }
}

private class CameraCapturedPreviewScreen(
    val image: ImageBitmap,
    val bindingBitmap: MutableState<ImageBitmap?>,
    val sheetCloseHandle: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            println("Selected image: ${image.width} x ${image.height}")
        }
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
                    bitmap = image,
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
                    bindingBitmap.value = image
                    sheetCloseHandle()
                }.size(24.dp), Color.White)
            }
        }
    }
}