package data

import androidx.compose.runtime.Composable
import lifemark_kmm.composeapp.generated.resources.Res
import lifemark_kmm.composeapp.generated.resources.example_documentation
import lifemark_kmm.composeapp.generated.resources.leading_html
import lifemark_kmm.composeapp.generated.resources.trailing_html
import lifemark_kmm.composeapp.generated.resources.webview_user_agent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
object ExperimentalMarkDownStaticData {
  val WEBVIEW_USER_AGENT: String @Composable get() = stringResource(Res.string.webview_user_agent)

    private val LEADING_HTML: String @Composable get() = stringResource(Res.string.leading_html)


    private val TRAILING_HTML: String @Composable get() = stringResource(Res.string.trailing_html)

    @Composable
    fun xHTMLContainer(content: String): String {
        return LEADING_HTML.trimIndent() + content + TRAILING_HTML.trimIndent()
    }

    val EXAMPLE_DOCUMENTATION: String @Composable get() = stringResource(Res.string.example_documentation)

//    private const val UnusableMarkDownTable: String = """
//| KEY                   | TYPE                            | DESCRIPTION                            |
//| --------------------- | ------------------------------- | -------------------------------------- |
//| ***id***              | ```string(${'$'}uuid)```        | Unique identifier of the account       |
//| ***createDate***      | ```string(${'$'}-time)```       | Date of account creation               |
//| ***sobriquet***       | ```string```                    | Personalized sobriquet for the account |
//| ***userCredentials*** | ```[Object]```                  | User credential object                 |
//| ├─ *username*         | ```string```                    | Account username                       |
//| ├─ *email*            | ```string & null```             | Account associated email               |
//| └─ *password*         | ```string```                    | Account Local Credentials              |
//| ***activeStatus***    | ```[Object]```                  | Account information object             |
//| └─ *status*           | ```string```                    | Account blockage information           |
//| ***level***           | ```[Object]```                  | Basic account information              |
//| ├─ *level*            | ```number(${'$'}int)```         | Account level                          |
//| └─ *impressionPoints* | ```number(${'$'}double)```      | Account available points               |
//| ***currentActivity*** | ```string(${'$'}uuid) & null``` | Current sharing status                 |
//"""
}
