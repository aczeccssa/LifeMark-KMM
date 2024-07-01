package screens.experimental

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import components.ColorAssets
import components.ColumnRoundedContainer
import components.LargeButton
import components.NavigationHeader
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Checkmark
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import data.platform.MediaManageStore
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.PlatformDirectory
import io.github.vinceglb.filekit.core.PlatformFile
import io.github.vinceglb.filekit.core.baseName
import io.github.vinceglb.filekit.core.extension
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import screens.NAVIGATION_BAR_HEIGHT
import viewmodel.SnapAlertViewModel

object ExperimentalFileKitScreen : Screen {
    @Composable
    override fun Content() {
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight

        Surface {
            NavigationHeader("FileKit Picker", NavigationHeaderConfiguration.clearConfiguration)

            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding)
                    .padding(top = topOffset, bottom = NAVIGATION_BAR_HEIGHT),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { FileKitComponent() }
        }
    }
}

@Composable
private fun FileKitComponent() {
    var files: Set<PlatformFile> by remember { mutableStateOf(emptySet()) }
    var directory: PlatformDirectory? by remember { mutableStateOf(null) }

    val singleFilePicker = rememberFilePickerLauncher(
        PickerType.Image,
        "Single file picker",
        directory?.path
    ) { file ->
        file?.let { files += it }
    }

    val multipleFilesPicker = rememberFilePickerLauncher(
        PickerType.Image,
        PickerMode.Multiple,
        "Multiple files picker",
        directory?.path
    ) { file ->
        file?.let { files += it }
    }

    val filePicker = rememberFilePickerLauncher(
        PickerType.File(listOf("png")),
        "Single file picker, only png",
        directory?.path
    ) { file ->
        file?.let { files += it }
    }

    val filesPicker = rememberFilePickerLauncher(
        PickerType.File(listOf("png")),
        PickerMode.Multiple,
        "Multiple files picker, only png",
        directory?.path
    ) { file ->
        file?.let { files += it }
    }

    val directoryPicker =
        rememberDirectoryPickerLauncher("Directory picker", directory?.path) { dir ->
            directory = dir
        }

    val saver = rememberFileSaverLauncher { file ->
        file?.let { files += it }
    }

    val scope = rememberCoroutineScope()
    fun saveFile(file: PlatformFile) {
        scope.launch {
            saver.launch(file.readBytes(), file.baseName, file.extension, directory?.path)
        }
    }

    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpecificConfiguration.defaultContentPadding)
        ) {
            ColumnRoundedContainer(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LargeButton("Single file picker") { singleFilePicker.launch() }

                LargeButton("Multiple files picker") { multipleFilesPicker.launch() }

                LargeButton("Single file picker, only png") { filePicker.launch() }

                LargeButton("Multiple files picker, only png") { filesPicker.launch() }

                if (FileKit.isDirectoryPickerSupported()) LargeButton("Directory picker") { directoryPicker.launch() }
            }


            if (FileKit.isDirectoryPickerSupported())
                Text("Selected directory: ${directory?.path ?: "None"}")
            else Text("Directory picker is not supported")


            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 128.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(files.toList().size) {
                    PhotoItem(file = files.toList()[it], onSaveFile = ::saveFile)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PhotoItem(file: PlatformFile, onSaveFile: (PlatformFile) -> Unit) {
    var bytes by remember(file) { mutableStateOf<ByteArray?>(null) }
    var showName by remember { mutableStateOf(false) }

    LaunchedEffect(file) {
        bytes = file.readBytes()
    }

    Surface(
        onClick = { showName = !showName },
        modifier = Modifier.aspectRatio(1f).clip(shape = MaterialTheme.shapes.medium)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            bytes?.let {
                KamelImage(
                    asyncPainterResource(it),
                    contentDescription = file.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()

                )
            }

            Surface(
                color = ColorAssets.SurfaceVariant.value,
                shape = CircleShape,
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
            ) {
                IconButton(
                    onClick = {
                        bytes?.let {
                            MediaManageStore.storageImageToPhotoLibrary(it)
                            SnapAlertViewModel.pushSnapAlert("Success to save image to photo library.")
                        } ?: onSaveFile(file)
                    },
                    modifier = Modifier.size(30.dp),
                ) {
                    Icon(
                        EvaIcons.Outline.Checkmark,
                        modifier = Modifier.size(18.dp),
                        contentDescription = "Save",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }

            AnimatedVisibility(
                visible = showName, modifier = Modifier.padding(4.dp).align(Alignment.BottomStart)
            ) {
                Surface(
                    color = MaterialTheme.colors.surface.copy(alpha = 0.7f),
                    shape = MaterialTheme.shapes.small,
                ) {
                    Text(
                        file.name,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}