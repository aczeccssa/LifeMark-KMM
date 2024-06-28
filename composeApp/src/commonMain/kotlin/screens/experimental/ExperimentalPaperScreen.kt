package screens.experimental

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import components.NavigationHeader
import data.NavigationHeaderConfiguration
import data.SpecificConfiguration
import screens.NAVIGATION_BAR_HEIGHT

object ExperimentalPaperScreen : Screen {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        // TODO: Paper
        val pagerState = rememberPagerState(pageCount = { 3 }, initialPage = 0)

        // Properties
        val scrollState = rememberScrollState()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.calculateHeight

        Surface {
            NavigationHeader(
                title = "Experimental Paper",
                configuration = NavigationHeaderConfiguration.transparentConfiguration
            )

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = SpecificConfiguration.defaultContentPadding)
                    .padding(top = topOffset, bottom = NAVIGATION_BAR_HEIGHT),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(pagerState) { index ->
                    // when(index) {
                        // else -> suspend { pagerState.scrollToPage(1) }
                    // }
                    StateView(index)
                }
            }
        }
    }
}

@Composable
private fun StateView(index: Int) {
    Box(Modifier.fillMaxSize(), Alignment.Center) {
        Text("Index: $index")
    }
}