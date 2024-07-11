package components.screens


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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.picture_selector.compose.rememberPictureSelect
import com.usecase.picture_selector.Media
import com.usecase.picture_selector.PictureSelectParams
import components.navigator.AppPages
import components.navigator.ComponentPages
import components.screens.haze.HazeSamples
import data.dragOffsetHandler
import data.models.PhotoScreenModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(screenModel: PhotoScreenModel) {
    // 这里是HomeScreen页面的内容
    val pagerState = rememberPagerState(
        pageCount = { AppPages.entries.size }, initialPage = AppPages.PAGE_TWO.ordinal
    )
    val coroutineScope = rememberCoroutineScope()

    Surface {
        Column {

            Box(
                modifier = Modifier.fillMaxSize().weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { paddingValues ->
                    Column(modifier = Modifier.padding(paddingValues)) {
                        // 此处顶部栏样式，可扩展为 -> 自定义+TabRow+自定义
                        TabRow(selectedTabIndex = pagerState.currentPage) {
                            AppPages.entries.forEachIndexed { index, page ->
                                Tab(selected = pagerState.currentPage == index, onClick = {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }, text = {
                                    Text(
                                        page.title, fontSize = 12.sp
                                    )
                                }, icon = {
                                    Icon(
                                        imageVector = if (pagerState.currentPage == index) page.selectImage else page.image,
                                        "pageIcon",
                                        modifier = Modifier.size(24.dp)
                                    )
                                })
                            }
                        }
                        HorizontalPager(state = pagerState) { page ->
                            when (AppPages.entries[page]) {
                                AppPages.PAGE_ONE -> PageOneContent(screenModel)
                                AppPages.PAGE_TWO -> PageTwoContent()
                                AppPages.PAGE_THEE -> PageThrContent(screenModel)
                            }
                        }
                    }
                }
            }

        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatContent() {
    // 这里是ChatScreen页面的内容
    val paperState = rememberPagerState(
        pageCount = { ComponentPages.entries.size },
        initialPage = ComponentPages.entries.first().ordinal
    )
    val coroutineScope = rememberCoroutineScope()

    TabRow(selectedTabIndex = paperState.currentPage) {
        ComponentPages.entries.forEachIndexed { index, screenType ->
            Tab(
                selected = paperState.currentPage == index,
                onClick = { coroutineScope.launch { paperState.animateScrollToPage(index) } },
            ) {
                Text(screenType.title)
            }
        }
    }
    HorizontalPager(state = paperState) { page ->
        when (ComponentPages.entries[page]) {
            ComponentPages.PAGE_HAZE -> HazeSamples("HazeSample")
            ComponentPages.PAGE_OTHER -> ""
        }
    }

}


@Composable
fun PushContent(screenModel: PhotoScreenModel) {

    val scope = rememberCoroutineScope()

    /**
     *  Media data
     *  1. system album controller
     *  2. show grid list ui
     *  3. edit each item
     *  4. save all system data
     */
    val pictureSelector = rememberPictureSelect()
    val mediaList = remember { mutableStateListOf<Media?>(null) }

    /**
     *  MediaListPreview
     *  1. show state controller
     *  2. select item page index
     */
    var mediaPreviewState by remember { mutableStateOf(false) }
    var pageIndex by remember { mutableStateOf<Int>(0) }

    Scaffold(modifier = Modifier.statusBarsPadding()) {
        Box {
            Column(
                modifier = Modifier.fillMaxSize().background(Color.LightGray).verticalScroll(
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
                                modifier = Modifier.fillMaxSize().background(Color.White)
                                    .clickable {
                                        scope.launch {
                                            pictureSelector.selectPhoto(
                                                params = PictureSelectParams(
                                                    maxImageNum = 1, maxVideoNum = 9, isCrop = true
                                                )
                                            ).collect {
                                                it.getOrNull()?.let { listMedia ->
                                                    // 将选择的媒体列表收集到 listPic 中
                                                    mediaList.clear() // 清空旧的列表
                                                    mediaList.addAll(listMedia) // 添加新的媒体列表
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
                        Text("text: ${it?.name}, path:${it?.path}")
                    }
                    Spacer(Modifier.weight(1f))
                    Row(modifier = Modifier) {
                        Button(onClick = {
                            // 过滤掉 mediaList 中的 null 值，并转换为 List<Media>
                            val mediaListNotNull = mediaList.filterNotNull()
                            screenModel.uploadPicture(mediaListNotNull)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaListPreview(
    media:List<Media>,
    index: Int,
    onDismiss: () -> Unit
){
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

@Composable
fun MediaPreview(
    byteArray: ByteArray,
    onDismiss: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black).clickable { onDismiss() }) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier.dragOffsetHandler { onDismiss() }) {
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

@Composable
fun SettingsContent() {
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.background(Color.LightGray).size(100.dp).clickable {
                showDialog = true
            })
        }

        if (showDialog) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black)
//                    .dragOffsetHandler(onDismiss = { showDialog = false })
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(modifier = Modifier.background(Color.Gray).size(300.dp)
                        .clickable { showDialog = false })
                }
            }
        }
    }
}