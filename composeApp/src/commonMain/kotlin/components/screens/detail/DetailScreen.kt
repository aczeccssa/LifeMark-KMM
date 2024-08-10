package components.screens.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.screens.EmptyScreenContent
import data.models.PostObject
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

data class DetailScreen(val obj: PostObject): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: DetailScreenModel = getScreenModel()

        //val obj by screenModel.getPost(postId).collectAsState(initial = null)
        println("DetailScreen -> obj:$obj")
        AnimatedContent(true) { objectAvailable ->
            if (objectAvailable) {
                ObjectDetails(obj, onBackClick = { navigator.pop() })
            } else {
                EmptyScreenContent(Modifier.fillMaxSize())
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ObjectDetails(
    obj: PostObject,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar({ IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack,"返回" ) }})
        }
    ) {paddingValues ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            val pageState = rememberPagerState (pageCount = { obj.imageUrl.size })
            HorizontalPager(state = pageState) { page ->
                obj.imageUrl[page]?.let { asyncPainterResource(data = "http://10.11.145.242:8080/uploads/$it") }?.let {
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
                // 遍历obj.imageUrl.size每个page页面显示对应的imageUrl的KamelImage
            }

            Column(Modifier.padding(12.dp)) {
                obj.title?.let { Text(it, style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)) }
                obj.content?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }
}
