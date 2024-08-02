package screens.merge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.LargeButton
import components.SurfaceColors
import components.secondaryButtonColors
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.ColorPicker
import io.kamel.core.Resource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

class Login : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Surface(color = MaterialTheme.colorScheme.surface) {
            Box(Modifier.fillMaxSize()) {
                KamelImage(
                    resource = asyncPainterResource(data = ""),
                    contentDescription = "login",
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CardWithHaze { LoginForm() }
                    // 登录方式快捷键
                    Row() {
                        Icon(
                            imageVector = EvaIcons.Outline.ColorPicker,
                            contentDescription = "login"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardWithHaze(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.height(150.dp).width(280.dp)) { content() }
    }

}

@Composable
fun LoginForm() {

    // 输入框
    var text by rememberSaveable { mutableStateOf("") }

    OutlinedTextField(shape = RoundedCornerShape(28.dp),modifier = Modifier.padding(8.dp),
        singleLine = true,
        value = text, onValueChange = { text = it }, label = { Text("Label") })

    Spacer(modifier = Modifier.height(18.dp))
    // 按钮
    LargeButton(
        text = "Login/Register",
        clip = RoundedCornerShape(12.dp),
        colors = SurfaceColors.secondaryButtonColors,
    ) { }

}

@Preview
@Composable
fun DefaultPreview() {
    Login()
}