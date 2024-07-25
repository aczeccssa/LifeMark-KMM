package screens.merge

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.picture_selector.compose.rememberPictureSelect
import com.usecase.picture_selector.Media
import com.usecase.picture_selector.PictureSelectParams
import components.navigator.AppPages
import components.navigator.ComponentPages
import components.screens.detail.DetailScreen
import components.screens.haze.HazeSamples
import data.dragOffsetHandler
import data.models.PhotoObject
import data.models.PhotoScreenModel
import data.models.Post
import data.models.PostObject
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
class PublishPost : Screen {
    @Composable
    override fun Content() {

        val scope = rememberCoroutineScope()
        val pictureSelector = rememberPictureSelect()
        val mediaList = remember { mutableStateListOf<Media?>(null) }

        var mediaPreviewState by remember { mutableStateOf(false) }
        var pageIndex by remember { mutableStateOf<Int>(0) }

        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }

        val screenModel: PhotoScreenModel = getScreenModel()

        Scaffold(modifier = Modifier.statusBarsPadding()) {
            Box {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color.LightGray)
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {
                    // c
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 静态项：触发图片选择器的按钮
                        item {
                            Box(
                                modifier = Modifier.size(100.dp)
                                    .clip(shape = RoundedCornerShape(14.dp)) // 添加圆角效果，14.dp 是圆角的尺寸
                            ) {
                                Icon(imageVector = Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = Color.LightGray,
                                    modifier = Modifier.fillMaxSize()
                                        .background(Color.White)
                                        .clickable {
                                            scope.launch {
                                                pictureSelector.selectPhoto(
                                                    params = PictureSelectParams(
                                                        maxImageNum = 1,
                                                        maxVideoNum = 9,
                                                        isCrop = true
                                                    )
                                                ).collect {
                                                    it.getOrNull()?.let { listMedia ->
                                                        // 将选择的媒体列表收集到 listPic 中
                                                        mediaList.clear() // 清空旧的列表
                                                        mediaList.addAll(listMedia) // 添加新的媒体列表
                                                        // 立即更新 postState
                                                        //postState = postState.copy(files = mediaList.filterNotNull())
                                                    }
                                                }
                                            }
                                        })
                            }

                        }
                        itemsIndexed(mediaList) { index, it ->
                            if (it != null) {
                                PictureItem(
                                    byteArray = it.preview.toByteArray(),
                                    onDismiss = { mediaList.removeAt(index) },
                                    onClick = {
                                        mediaPreviewState = !mediaPreviewState
                                        pageIndex = index
                                    })
                            }
                        }
                    }

                    // content
                    Column {
                        mediaList.forEach {
                            Text("name: ${it?.name}, path:${it?.path}")
                        }

                        // 单行文本输入框
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true // 确保 TextField 是单行的
                        )

                        // 多行文本输入框
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp), // 指定最小高度，可以根据需要调整
                            maxLines = Int.MAX_VALUE // 允许多行文本输入
                        )

                        Row(modifier = Modifier) {
                            Button(onClick = {
                                val post = Post(title = title, description = description, files = mediaList.toList())
                                screenModel.uploadPicture(post)
                            }) {
                                Text("POST MEDIA")
                            }
                        }

                    }


                }

                // preview
                if (mediaPreviewState) {
                    MediaListPreview(
                        media = mediaList.filterNotNull(),
                        index = pageIndex,
                        onDismiss = { mediaPreviewState = false }
                    )
                }
            }
        }
    }
}


@Composable
fun PictureItem(
    byteArray: ByteArray,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier.size(100.dp).dragOffsetHandler(onDismiss = onDismiss)
            .clickable { onClick() }.clip(shape = RoundedCornerShape(14.dp)) // 添加圆角效果，14.dp 是圆角的尺寸
    ) {
        AsyncImage(
            model = byteArray,
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp).aspectRatio(1f)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaListPreview(
    media: List<Media>,
    index: Int,
    onDismiss: () -> Unit
) {
    // 预先转换所有 Media 对象的 preview 属性到字节数组，并创建映射表
    val byteArrayList = remember(media) {
        media.map { it.preview.toByteArray() }
    }

    val pagerState = rememberPagerState(pageCount = { media.size }, initialPage = index)
    Box(modifier = Modifier.fillMaxSize().background(Color.Black).clickable { onDismiss() }) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier.dragOffsetHandler { onDismiss() }) {
                HorizontalPager(state = pagerState) { index ->
                    // 从映射表中获取对应的字节数组
                    val byteArray = byteArrayList[index]
                    AsyncImage(
                        model = byteArray,
                        contentDescription = "Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                    )
                }
            }
        }
    }
}