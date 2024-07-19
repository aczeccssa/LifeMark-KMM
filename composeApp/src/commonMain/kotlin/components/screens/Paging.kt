package components.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.screens.detail.DetailScreenModel
import components.screens.detail.ObjectDetails
import data.models.PostObject
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingState
import app.cash.paging.CombinedLoadStates
import app.cash.paging.LoadState
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import data.models.PhotoObject
import data.models.PhotoScreenModel
import org.jetbrains.compose.resources.ExperimentalResourceApi

data class MyDataItem(val id: Int, val content: String)

val staticDataList = buildList {
    for (i in 1..300) {
        add(MyDataItem(i, "Item $i"))
    }
}

class Paging : Screen {
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val screenModel: PhotoScreenModel = getScreenModel()
        val objects by screenModel.objects.collectAsState()
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Paging") },
                    modifier = Modifier.padding(top = 36.dp),
                    elevation = 0.dp,
                    navigationIcon = {
                        IconButton(onClick = { println("Drawer clicked") }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { println("Search Internships!") }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    backgroundColor = Color.White
                )
            },
            drawerContent = { /*Drawer content*/ },
            content = {
                if (objects.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
                    { CircularProgressIndicator() }
                } else {
                    println("objects -> objects.size: ${objects.size}")
                    val pager = remember {
                        Pager(
                            PagingConfig(
                                pageSize = 5,
                                initialLoadSize = 0,
                                maxSize = objects.size
                            )
                        ) { StaticPagingSource(objects) }
                    }

                    // 使用 pager.flow 获取分页数据流
                    val pagingData = pager.flow.collectAsLazyPagingItems()
                    LaunchedEffect(screenModel.objects) {
                        screenModel.objects.collect { newObjects ->
                            println("objects updated: $newObjects")
                            pagingData.refresh() // 刷新分页器
                        }
                    }
                    println("pager -> $pager pagingData: ${pagingData.itemCount}")
                    PagingListUI(data = pagingData, content = { PageCard(it) })
                }
            },
        )
    }
}

class StaticPagingSource(val data: List<PhotoObject>) : PagingSource<Int, PhotoObject>() {
    override fun getRefreshKey(state: PagingState<Int, PhotoObject>): Int? = null


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoObject> {
        // 当前页起始位置
        val _page = params.key ?: 0
        val startOffset = _page * params.loadSize
        val endOffset = startOffset + params.loadSize
        // 当前页数据
        val loadData = data.subList(startOffset, endOffset)
        // 判断是否有更多数据可以加载
        val hasNextPage = endOffset + params.loadSize < data.size
        println("load -> _page: $_page, startOffset: $startOffset, endOffset: $endOffset, hasNextPage: $hasNextPage")
        return LoadResult.Page(
            data = loadData,
            prevKey = if (_page > 0) _page - 1 else null,
            nextKey = if (hasNextPage) _page + 1 else null
        )
    }
}

@ExperimentalResourceApi
@Composable
fun PageCard(page: PhotoObject) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            page.title.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun <T : Any> PagingListUI(
    data: LazyPagingItems<T>,
    content: @Composable (T) -> Unit
) {
    Column {
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {}
            Column(modifier = Modifier.weight(1f)) {}
        }
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(data.itemCount) { index ->
            Column(Modifier.border(1.dp, Color.LightGray)) {
                Row(Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        val item = data[index]
                        item?.let { content(it) }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        val parity = index + 1
                        if (parity < data.itemCount) {
                            val item = data[index + 1]
                            item?.let { content(it) }
                        }

                    }
                }

            }

        }
    }

    // 处理加载状态
    handleLoadState(data.loadState)
}

@Composable
private fun handleLoadState(loadState: CombinedLoadStates) {
//    when {
//        loadState is LoadState.NotLoading && loadState.endOfPaginationReached -> {
//            // 没有更多数据
//        }
//        loadState is LoadState.NotLoading && !loadState.endOfPaginationReached -> {
//            // 可以加载更多数据
//        }
//        loadState is LoadState.Loading -> {
//            // 显示加载指示器
//        }
//        loadState is LoadState.Error -> {
//            // 显示错误信息
//        }
//    }
}

//@Composable
//fun <T : Any> PagingListUI(
//    data: LazyPagingItems<T>,
//    content: @Composable (T) -> Unit
//) {
//    Row(modifier = Modifier.fillMaxSize()) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize().weight(1f)
//                .background(Color.White),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            items(data.itemCount) { index ->
//                if (index % 2 == 0) {
//                    val item = data[index]
//                    item?.let { content(it) }
//                }
//            }
//            data.loadState.apply {
//                when {
//                    refresh is LoadStateNotLoading && data.itemCount < 1 -> {
//                        item {
//                            Box(
//                                modifier = Modifier.fillParentMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text(
//                                    text = "No Items",
//                                    modifier = Modifier.align(Alignment.Center),
//                                    textAlign = TextAlign.Center
//                                )
//                            }
//                        }
//                    }
//
//                    refresh is LoadStateLoading -> {
//                        item {
//                            Box(
//                                modifier = Modifier.fillParentMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                CircularProgressIndicator(
//                                    color = Color.Transparent
//                                )
//                            }
//                        }
//                    }
//
//                    append is LoadStateLoading -> {
//                        item {
//                            CircularProgressIndicator(
//                                color = Color.Transparent,
//                                modifier = Modifier.fillMaxWidth()
//                                    .padding(16.dp)
//                                    .wrapContentWidth(Alignment.CenterHorizontally)
//                            )
//                        }
//                    }
//
//                    refresh is LoadStateError -> {
//                        item {
//                            ErrorView(
//                                message = "No Internet Connection.",
//                                onClickRetry = { data.retry() },
//                                modifier = Modifier.fillParentMaxSize()
//                            )
//                        }
//                    }
//
//                    append is LoadStateError -> {
//                        item {
//                            ErrorItem(
//                                message = "No Internet Connection",
//                                onClickRetry = { data.retry() },
//                            )
//                        }
//                    }
//                }
//            }
//        }
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize().weight(1f)
//                .background(Color.White),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            items(data.itemCount) { index ->
//                if (index %2 == 1) {
//                    val item = data[index]
//                    item?.let { content(it) }
//                }
//            }
//            data.loadState.apply {
//                when {
//                    refresh is LoadStateNotLoading && data.itemCount < 1 -> {
//                        item {
//                            Box(
//                                modifier = Modifier.fillParentMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text(
//                                    text = "No Items",
//                                    modifier = Modifier.align(Alignment.Center),
//                                    textAlign = TextAlign.Center
//                                )
//                            }
//                        }
//                    }
//
//                    refresh is LoadStateLoading -> {
//                        item {
//                            Box(
//                                modifier = Modifier.fillParentMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                CircularProgressIndicator(
//                                    color = Color.Transparent
//                                )
//                            }
//                        }
//                    }
//
//                    append is LoadStateLoading -> {
//                        item {
//                            CircularProgressIndicator(
//                                color = Color.Transparent,
//                                modifier = Modifier.fillMaxWidth()
//                                    .padding(16.dp)
//                                    .wrapContentWidth(Alignment.CenterHorizontally)
//                            )
//                        }
//                    }
//
//                    refresh is LoadStateError -> {
//                        item {
//                            ErrorView(
//                                message = "No Internet Connection.",
//                                onClickRetry = { data.retry() },
//                                modifier = Modifier.fillParentMaxSize()
//                            )
//                        }
//                    }
//
//                    append is LoadStateError -> {
//                        item {
//                            ErrorItem(
//                                message = "No Internet Connection",
//                                onClickRetry = { data.retry() },
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//}


@Composable
private fun ErrorItem(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            maxLines = 1,
            modifier = Modifier.weight(1f),
            color = Color.Red
        )
        OutlinedButton(onClick = onClickRetry) {
            Text(text = "Try again")
        }
    }
}

@Composable
private fun ErrorView(
    message: String,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp).onPlaced { _ ->
        },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            maxLines = 1,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.Red
        )
        OutlinedButton(
            onClick = onClickRetry, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentWidth(Alignment.CenterHorizontally)
        ) {
            Text(text = "Try again")
        }
    }
}