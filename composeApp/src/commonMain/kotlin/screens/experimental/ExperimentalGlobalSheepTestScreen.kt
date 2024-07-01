package screens.experimental

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import components.ColorAssets
import components.NavigationHeader
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Star
import data.NavigationHeaderConfiguration
import data.units.now
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import utils.MediaLinkCache

object ExperimentalGlobalSheepTestScreen : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight

        Surface {
            NavigationHeader("Global Sheep", NavigationHeaderConfiguration.transparentConfiguration)

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                    .background(Color(0xFFD7D7D7)).padding(4.dp).padding(top = topOffset),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SheepCard(
                    SheepCardData(
                        avatar = MediaLinkCache.randomAvatar().toString(),
                        title = "Sally Summers",
                        subtitle = "Lost in Lagos Portugal",
                        date = LocalDateTime.now(),
                        cover = MediaLinkCache.randomAvatar().toString()
                    )
                )
            }
        }
    }

    @Composable
    private fun SheepCard(data: SheepCardData) {
        Column(
            modifier = Modifier.fillMaxWidth().height(520.dp).clip(RoundedCornerShape(26.dp))
                .background(MaterialTheme.colors.surface).padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                KamelImage(
                    resource = asyncPainterResource(data.avatar),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(36.dp).clip(CircleShape)
                )

                Icon(
                    imageVector = EvaIcons.Fill.Star,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = ColorAssets.LightGray.value
                )
            }

            Column(Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.Bottom) {
                    Column {
                        Text(
                            text = data.title,
                            style = MaterialTheme.typography.h4,
                            maxLines = 2,
                            modifier = Modifier.fillMaxWidth(40f)
                        )
                        Text(
                            data.subtitle,
                            style = MaterialTheme.typography.caption,
                            color = ColorAssets.Gray.value
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "${data.date.hour}:${data.date.minute}",
                            style = MaterialTheme.typography.h6
                        ) // Content Color, 22.sp
                        Text(
                            "${data.date.dayOfMonth}.${data.date.month}.${data.date.year}",
                            style = MaterialTheme.typography.caption,
                            color = ColorAssets.Gray.value
                        ) // Sub color, 14.sp
                    }
                }

                Spacer(Modifier.height(12.dp))

                KamelImage(
                    resource = asyncPainterResource(data.cover),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(260.dp)
                        .clip(RoundedCornerShape(26.dp))
                )
            }
        }
    }
}

@Serializable
data class SheepCardData(
    val avatar: String, // User avatar url
    val title: String, // Sheep title
    val subtitle: String, // Sheep sub title
    val date: LocalDateTime, // Sheep create time
    val cover: String // Cover image url
)