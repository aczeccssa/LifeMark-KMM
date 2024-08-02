package components.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import com.picture_selector.compose.rememberPictureSelect
import com.usecase.picture_selector.Media
import com.usecase.picture_selector.PictureSelectParams
import data.lottie.Lottie_Chicken
import data.models.PhotoObject
import data.models.PostScreenModel
import io.github.alexzhirkevich.compottie.LottieAnimation
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.LottieConstants
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun PageOneContent(screenModel: PostScreenModel) {
    val scope = rememberCoroutineScope()
    val pictureSelector = rememberPictureSelect()
    Scaffold(modifier = Modifier.statusBarsPadding()) {
        var pictureMedia by remember { mutableStateOf<Media?>(null) }
        val listPic = remember { mutableStateListOf<Media?>(null) }

        LaunchedEffect(listPic.size) {
            println("Current : ${listPic.size}")
        }

        Column {
            Button(onClick = {
                scope.launch {
                    pictureMedia =
                        pictureSelector.takePhoto(
                            params = PictureSelectParams(
                                maxImageNum = 1,
                                isCrop = true
                            )
                        )
                            .firstOrNull()
                            ?.getOrNull()
                }
            }) {
                Text("拍摄照片", modifier = Modifier.padding(horizontal = 24.dp))
            }
            Button(onClick = {
                scope.launch {
                    pictureMedia =
                        pictureSelector.takePhoto(
                            params = PictureSelectParams(
                                maxImageNum = 0,
                                maxVideoNum = 2
                            )
                        ).firstOrNull()
                            ?.getOrNull()
                }
            }) {
                Text("拍摄视频", modifier = Modifier.padding(horizontal = 24.dp))
            }
            Button(onClick = {
                scope.launch {
                    pictureSelector.selectPhoto(
                        params = PictureSelectParams(
                            maxImageNum = 1,
                            maxVideoNum = 9,
                            isCrop = true
                        )
                    ).collect {
                        it.getOrNull()?.let { mediaList ->
                            // 将选择的媒体列表收集到 listPic 中
                            listPic.clear() // 清空旧的列表
                            listPic.addAll(mediaList) // 添加新的媒体列表
                        }
                    }
                }
            }) {
                Text("选择图库", modifier = Modifier.padding(horizontal = 24.dp))
            }
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text("path:${pictureMedia?.path}")
                AsyncImage(
                    model = pictureMedia?.preview?.toByteArray(),
                    contentDescription = "Image",
                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                )
                listPic.forEach {
                    println("listPic: $it")
                    AsyncImage(
                        model = it?.preview?.toByteArray(),
                        contentDescription = "Image",
                        modifier = Modifier.fillMaxWidth().wrapContentHeight()
                    )
                }
            }
        }

    }
}

@Composable
fun PageTwoContent() {
    // Your content for Page Two here
    Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("This is Page Two")
            Loader()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageThrContent(screenModel: PostScreenModel) {

    val navigator = LocalNavigator.currentOrThrow
    val objects by screenModel.objects.collectAsState()


    // Your content for Page Two here
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(objects.isNotEmpty()) { objectsAvailable ->
            if (objectsAvailable) {
                ObjectGrid(
                    objects = objects,
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

@Composable
fun ObjectGrid(
    objects: List<PhotoObject>,
    onObjectClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(objects, key = { it.objectID }) { obj ->
            ObjectFrame(
                obj = obj,
                onClick = { onObjectClick(obj.objectID) }
            )
        }
    }
}

@Composable
fun ObjectFrame(
    obj: PhotoObject,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .padding(6.dp)
            .clickable { onClick() }
    ) {
        KamelImage(
            resource = asyncPainterResource(data = obj.primaryImageSmall),
            contentDescription = obj.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.LightGray)
        )

        Spacer(Modifier.height(2.dp))

        Text(obj.title)
        Text(obj.artistDisplayName)
        Text(obj.objectDate)
    }
}

@Composable
fun Loader() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.JsonString(
            Lottie_Chicken
        )
    )
    val progress by animateLottieCompositionAsState(composition)
//    LottieAnimation(
//        composition = composition,
//        progress = {progress},
//    )
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
}

@Composable
fun EmptyScreenContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text("没有数据！请检查网络")
    }
}