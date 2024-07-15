package components.screens


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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import data.models.PhotoObject
import data.models.PhotoScreenModel
import data.models.Post
import data.models.PostObject
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
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


@OptIn(ExperimentalFoundationApi::class)
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
    val postObject by screenModel.temporaryStorage.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        pageCount = { 2 }, initialPage = 0
    )
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier) {
        // 此处顶部栏样式，可扩展为 -> 自定义+TabRow+自定义
        TabRow(selectedTabIndex = pagerState.currentPage) {

            Tab(
                selected = pagerState.currentPage == 0, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                },
                text = { Text("发布", fontSize = 12.sp) }
            )
            Tab(
                selected = pagerState.currentPage == 1, onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                },
                text = { Text("列表", fontSize = 12.sp) }
            )

        }
        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
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
                                            // test http server to client response
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(2)
                                            }
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
                1 -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        println("postObject collect: $postObject")
                        AnimatedContent(postObject.isNotEmpty()) { objectsAvailable ->
                            if (objectsAvailable) {
                                ObjectGrids(
                                    objects = postObject,
                                    onObjectClick = { objectId ->
//                        navigator.push(DetailScreen(objectId))
                                    }
                                )
                            } else {
                                EmptyScreenContent(Modifier.fillMaxSize())
                            }

                        }
                    }
                }
            }
        }
    }


}


@Composable
fun ObjectGrids(
    objects: List<PostObject>,
    onObjectClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(objects) { obj ->
            ObjectFrames(
                obj = obj,
                onClick = { }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ObjectFrames(
    obj: PostObject,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(6.dp)
            .clickable { onClick() }
    ) {
        obj.imageUrl.first()?.let { asyncPainterResource(data = "http://10.11.145.242:8080/uploads/$it") }?.let {
            println("KamelImage -> url:http://10.11.145.242:8080/uploads/$it")
            KamelImage(
                resource = it,
                contentDescription = obj.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.LightGray)
            )
        }
        val pageState = rememberPagerState (pageCount = { obj.imageUrl.size })
        HorizontalPager(state = pageState) { page ->

            // 遍历obj.imageUrl.size每个page页面显示对应的imageUrl的KamelImage
        }

        Spacer(Modifier.height(2.dp))
        obj.title?.let { androidx.compose.material.Text(it) }
        obj.content?.let { androidx.compose.material.Text(it) }
        Text("PostObject: $obj")
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