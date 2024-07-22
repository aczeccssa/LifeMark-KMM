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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import io.kamel.core.Resource
import io.kamel.core.utils.URL
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

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

@Composable
fun <T : Any> PagingListUI(
    data: LazyPagingItems<T>,
    content: @Composable (T) -> Unit
) {
    val oddItems = remember { mutableStateListOf<T>() }
    val evenItems = remember { mutableStateListOf<T>() }
    println("t1 -> ${data.itemSnapshotList.items}")
    LaunchedEffect(data.itemSnapshotList.items) {
        for (index in 0 until data.itemCount) {
            // 在这里使用 index
            if (index % 2 == 0) {
                data[index]?.let { evenItems.add(it) }
            } else {
                data[index]?.let { oddItems.add(it) }
            }
        }
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Odd Items")
                evenItems.forEach { item ->
                    content(item)
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Even Items")
                oddItems.forEach { item ->
                    content(item)
                }
            }
        }
    }
}


@ExperimentalResourceApi
@Composable
fun PageCard(page: PhotoObject) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp, 4.dp, 2.dp, 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            KamelImage(
                resource = asyncPainterResource(data = page.primaryImageSmall),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 20.dp,max = 120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                onLoading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
            )
            Text(
                text = page.objectID.toString(),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
            Text(
                text = page.title,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

