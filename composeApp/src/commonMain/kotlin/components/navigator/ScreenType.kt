package components.navigator

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Headphones
import compose.icons.evaicons.fill.Home
import compose.icons.evaicons.fill.MessageSquare
import compose.icons.evaicons.fill.Monitor
import compose.icons.evaicons.fill.Person
import compose.icons.evaicons.fill.PlusSquare
import compose.icons.evaicons.fill.Radio
import compose.icons.evaicons.outline.Headphones
import compose.icons.evaicons.outline.Home
import compose.icons.evaicons.outline.MessageSquare
import compose.icons.evaicons.outline.Monitor
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.Radio

enum class ScreenType(
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val buttonName: String
) {
    HOME_SCREEN(
        EvaIcons.Outline.Home,
        EvaIcons.Fill.Home,
        "首页"
    ),
    CHAT_SCREEN(
        EvaIcons.Outline.MessageSquare,
        EvaIcons.Fill.MessageSquare,
        "社区"
    ),
    PUSH_SCREEN(
        EvaIcons.Fill.PlusSquare,
        EvaIcons.Fill.PlusSquare,
        "发布"
    ),
    SET_SCREEN(
        EvaIcons.Outline.Person,
        EvaIcons.Fill.Person,
        "设置"
    );
}

enum class AppPages(val title: String, val image: ImageVector, val selectImage: ImageVector) {
    PAGE_ONE("游戏", EvaIcons.Outline.Monitor, EvaIcons.Fill.Monitor),
    PAGE_TWO("发现", EvaIcons.Outline.Radio, EvaIcons.Fill.Radio),
    PAGE_THEE("音乐", EvaIcons.Outline.Headphones, EvaIcons.Fill.Headphones)
}

enum class ComponentPages(val title: String) {
    PAGE_HAZE("高斯模糊"),
    PAGE_OTHER("其他")
}