package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData
import components.NavigationHeader
import data.ExperimentalMarkDownStaticData
import data.NavigationHeaderConfiguration
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import org.jetbrains.compose.ui.tooling.preview.Preview

object ExperimentalMarkDownScreen : Screen {
    // FIXME: intellij/markdown && kevinnzou/compose-webview-multiplatform.
    @Composable
    @Preview
    override fun Content() {
        val src = ExperimentalMarkDownStaticData.EXAMPLE_DOCUMENTATION
        val flavour = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(src)
        val html = HtmlGenerator(src, parsedTree, flavour).generateHtml()

        // MARK:
        //  The custom settings on library `kevinnzou/compose-webview-multiplatform` has so many
        //  different modifiable properties between each platform.
        val webViewState = rememberWebViewStateWithHTMLData(
            data = ExperimentalMarkDownStaticData.xHTMLContainer(html)
        )
        val webViewBackgroundColor = MaterialTheme.colors.background
        val navigator = rememberWebViewNavigator()
        val topOffset = NavigationHeaderConfiguration.defaultConfiguration.headerHeight + 28.dp

        DisposableEffect(Unit) {
            webViewState.webSettings.apply {
                // Web settings
                logSeverity = KLogSeverity.Debug
                customUserAgentString = ExperimentalMarkDownStaticData.WEBVIEW_USERAGENT
                backgroundColor = webViewBackgroundColor
                // Platform Only
                // MARK: Only iOS can controll the scroll indicator in this setting.
                iOSWebSettings.showHorizontalScrollIndicator = false
                androidWebSettings.apply { }
            }

            onDispose { }
        }

        Surface {
            NavigationHeader("Experimental markdown")

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    // TODO: Unknown how to made intellij/markdown generated html to conform dark mode.
                    .background(MaterialTheme.colors.background)
                    .padding(horizontal = 20.dp).padding(top = topOffset)
            ) {
                WebView(webViewState, modifier = Modifier.fillMaxSize(), navigator = navigator)
            }
        }
    }
}