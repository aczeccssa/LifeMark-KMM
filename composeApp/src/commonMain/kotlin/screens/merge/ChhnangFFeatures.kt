package screens.merge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import components.ColorAssets
import components.ListItem
import components.NavigationHeader
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ColorPicker
import data.NavigationHeaderConfiguration
import data.appNavigationBarPadding

object ChhnangFFeatures : Screen {
    @Composable
    override fun Content() {
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight

        Surface {
            NavigationHeader("FileKit Picker", NavigationHeaderConfiguration.clearConfiguration)

            Column(
                modifier = Modifier.verticalScroll(scrollState).fillMaxWidth()
                    .background(MaterialTheme.colors.background).padding(0.dp, 12.dp)
                    .appNavigationBarPadding().padding(top = topOffset),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                ListItem(
                    imageVector = EvaIcons.Outline.ColorPicker,
                    tint = ColorAssets.SK.FillOrange.value,
                    title = "简单示例",
                    sub = "效果参考设置页面"
                ) {
                    // navigator.push()
                }
            }
        }
    }
}