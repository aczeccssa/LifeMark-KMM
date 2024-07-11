package screens.merge

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import components.ColorAssets
import components.ListItem
import components.NavigationHeader
import components.navigator.ScreenType
import components.screens.ChatContent
import components.screens.HomeContent
import components.screens.PushContent
import components.screens.SettingsContent
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ColorPicker
import data.NavigationHeaderConfiguration
import data.appNavigationBarPadding
import data.models.PhotoScreenModel

object ChhnangFFeatures : Screen {
    @Composable
    override fun Content() {
        val (currentScreen, setCurrentScreen) = remember { mutableStateOf(ScreenType.HOME_SCREEN) }
        println("remember { mutableStateOf(ScreenType.HOME_SCREEN) } -> $currentScreen, $setCurrentScreen")
        Surface {
            Column(Modifier.background(androidx.compose.material3.MaterialTheme.colorScheme.background)) {
                // 内容区域，根据currentScreen显示不同的页面
                ContentScreen(currentScreen)
            }
            Box(
                Modifier.fillMaxSize().padding(bottom = 10.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                BottomBar(currentScreen, setCurrentScreen)
            }
        }
    }

    private @Composable
    fun ContentScreen(currentScreen: ScreenType) {
        val screenModel: PhotoScreenModel = getScreenModel()
        when (currentScreen) {
            ScreenType.HOME_SCREEN -> HomeContent(screenModel)
            ScreenType.CHAT_SCREEN -> ChatContent()
            ScreenType.PUSH_SCREEN -> PushContent(screenModel)
            ScreenType.SET_SCREEN -> SettingsContent()
        }
    }
}



@Composable
fun BottomBar(currentScreen: ScreenType, setCurrentScreen: (ScreenType) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(horizontal = 20.dp, vertical = 6.dp) // 外部padding
            //.shadow(elevation = 4.dp) // 在Box上添加阴影
            .clip(RoundedCornerShape(50)) // 使用Box裁剪形状
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.material3.MaterialTheme.colorScheme.surface) // 背景色
                .padding(horizontal = 20.dp, vertical = 6.dp), // 外部padding
            shape = RoundedCornerShape(50) // 设置圆角形状
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp), // 内部padding，防止按钮紧挨着边缘
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScreenType.entries.forEach { screenType ->
                    val isSelected = currentScreen == screenType
                    BottomBarItem(
                        selected = isSelected,
                        icon = if (isSelected) screenType.selectedIcon else screenType.unselectedIcon,
                        text = screenType.buttonName
                    ) { setCurrentScreen(screenType) }
                }
            }
        }
    }
}

@Composable
fun BottomBarItem(selected: Boolean, icon: ImageVector, text: String, onClick: () -> Unit) {

    val size by animateDpAsState(targetValue = if (selected) 22.dp else 18.dp) // 动画大小
    val iconColor by animateColorAsState(if (selected) androidx.compose.material3.MaterialTheme.colorScheme.primary else Color.Gray)
    val textColor by animateColorAsState(if (selected) androidx.compose.material3.MaterialTheme.colorScheme.primary else Color.Gray)
    // 定义基础的TextStyle，使用MaterialTheme.typography.body1作为起始点
    val baseTextStyle = androidx.compose.material3.MaterialTheme.typography.bodySmall

    // 根据selected的值来决定字体大小
    val textStyle = baseTextStyle.copy(
        fontSize = if (selected) 14.sp else 12.sp
    )
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .animateContentSize() // 添加动画效果
            .clickable(
                interactionSource = interactionSource,
                indication = null, // 可以自定义点击反馈效果
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconColor,
            modifier = Modifier.size(size) // 使用动画大小
        )
        Text(
            text = text,
            color = textColor,
            style = textStyle
        )
    }
}
