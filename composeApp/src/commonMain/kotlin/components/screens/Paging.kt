package components.screens

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingState
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import app.cash.paging.compose.collectAsLazyPagingItems
import data.models.PhotoScreenModel

data class MyDataItem(val id: Int, val content: String)

val staticDataList = buildList {
    for (i in 1..300) {
        add(MyDataItem(i, "Item $i"))
    }
}
class Paging : Screen {
    @Composable
    override fun Content() {
        val screenModel: PhotoScreenModel = getScreenModel()
        val pager = remember {
            Pager(
                PagingConfig(
                    pageSize = 30,
                    initialLoadSize = 5,
                    maxSize = staticDataList.size
                )
            ) { StaticPagingSource() }
        }
        // 使用 pager.flow 获取分页数据流
        val pagingData = pager.flow.collectAsLazyPagingItems()

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
                //PagingListUI(data = result, content = { InternshipCard(it) })
                LazyColumn {
                    items(pagingData.itemCount) { index ->
                        val item = pagingData.get(index)
                        Text(text = item?.content ?: "item $index")
                    }
                }
            },
        )

    }
}

class StaticPagingSource : PagingSource<Int, MyDataItem>() {
    private val data = staticDataList
    override fun getRefreshKey(state: PagingState<Int, MyDataItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.let { it + anchorPage.data.size }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyDataItem> {
        // 当前页起始位置
        val _page = params.key ?: 0
        val startOffset = _page * params.loadSize
        val endOffset = startOffset + params.loadSize
        // 当前页数据
        val loadData = data.subList(startOffset, endOffset)
        // 判断是否有更多数据可以加载
        val hasNextPage = endOffset < data.size

        return LoadResult.Page(
            data = loadData,
            prevKey = if (_page > 0) _page - 1 else null,
            nextKey = if (hasNextPage) _page + 1 else null
        )
    }
}