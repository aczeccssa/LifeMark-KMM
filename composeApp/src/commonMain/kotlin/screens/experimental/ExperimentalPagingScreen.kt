package screens.experimental

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.Pager
import androidx.paging.PagingConfig
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import components.NavigationHeader
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import data.appNavigationBarPadding
import data.models.Article
import data.models.ArticlePagingSource
import data.models.ITEMS_PER_PAGE

object ExperimentalPagingScreen : Screen {
    @Composable
    override fun Content() {
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight
        // Article provider.
        val pager = remember {
            Pager(PagingConfig(pageSize = ITEMS_PER_PAGE), null) { ArticlePagingSource() }
        }
        val flow = pager.flow.collectAsLazyPagingItems()

        Surface {
            NavigationHeader("Paging(${flow.itemCount})")

            LazyColumn(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding)
                    .padding(top = topOffset).appNavigationBarPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { items(flow.itemCount) { index -> flow[index]?.let { ArticleItem(it) } } }
        }
    }
}

@Composable
private fun ArticleItem(article: Article) {
    Column {
        Divider()
        Row {
            Text(article.id.toString())
            Column {
                Text(article.title)
                Text(article.description)
            }
            Text(article.created.toString())
        }
        Divider()
    }
}