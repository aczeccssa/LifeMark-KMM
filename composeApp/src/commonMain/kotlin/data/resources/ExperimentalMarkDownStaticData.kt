package data.resources

import androidx.compose.runtime.Composable
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.example_documentation
import lifemark_kmm.composeapp.generated.resources.leading_html
import lifemark_kmm.composeapp.generated.resources.trailing_html
import lifemark_kmm.composeapp.generated.resources.webview_user_agent
import org.jetbrains.compose.resources.stringResource

object ExperimentalMarkDownStaticData {
    val WEB_VIEW_USER_AGENT: String @Composable get() = stringResource(Res.string.webview_user_agent)

    private val LEADING_HTML: String @Composable get() = stringResource(Res.string.leading_html)

    private val TRAILING_HTML: String @Composable get() = stringResource(Res.string.trailing_html)

    @Composable
    fun xHTMLContainer(content: String): String {
        return LEADING_HTML.trimIndent() + content + TRAILING_HTML.trimIndent()
    }

    val EXAMPLE_DOCUMENTATION: String @Composable get() = stringResource(Res.string.example_documentation)
}