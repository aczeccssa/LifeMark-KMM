package screens.merge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import components.screens.haze.rememberRandomSampleImageUrl
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Eye
import compose.icons.evaicons.outline.Eye
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

class Login : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val scope = rememberCoroutineScope()

        val screenModel: PostScreenModel = getScreenModel()

        val loginModel: LoginViewModel = getScreenModel()

        val scaffoldState = rememberScaffoldState()

        val username = remember { mutableStateOf("") }

        val password = remember { mutableStateOf("") }

        var passwordVisibility by remember { mutableStateOf(false) }

        val isErrorEmailIcon = remember { mutableStateOf(false) }

        val isErrorEmailMessage = remember { mutableStateOf("Null") }

        val isErrorPasswordIcon = remember { mutableStateOf(false) }

        val isErrorPasswordMessage = remember { mutableStateOf("Null") }


        val checkboxDurum = remember { mutableStateOf(true) }

        val keyboardController = LocalSoftwareKeyboardController.current

        val icon = if (passwordVisibility)
            EvaIcons.Outline.Eye
        else
            EvaIcons.Fill.Eye

        val state = loginModel.state.value
        println("LoginScreen -> state $state")
        when (state.success) {
            0 -> {}

            1 -> { LaunchedEffect(key1 = Unit) { navigator.push(MuseumPaging()) } }

            202 -> { LaunchedEffect(key1 = Unit) { navigator.push(SignUp()) } }

        }

        Scaffold(
            scaffoldState = scaffoldState,
            snackbarHost = {
                SnackbarHost(it) {
                    Snackbar(
                        backgroundColor = Color.Red,
                        contentColor = Color.White,
                        actionColor = Color.White,
                        snackbarData = it
                    )
                }
            },

            content = {

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Column() {
                        // 巨大的欢迎用户icon
                        Column(modifier = Modifier.weight(1.3f)) {
                            KamelImage(
                                //resource = asyncPainterResource(data = "https://www.emojiall.com/en/header-svg/%F0%9F%91%8F.svg"),
                                resource = asyncPainterResource(data = rememberRandomSampleImageUrl(width = 800)),
                                contentDescription = "login",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(2.0f)
                                .fillMaxWidth()
                                .offset(y = -30.dp)
                                .background(
                                    color = Color.White,
                                    RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                                )

                        ) {
                            // 巨大的欢迎文本
                            Row(
                                horizontalArrangement = Arrangement.Center, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 15.dp)
                            ) {

                                Text(
                                    text = "Login/Register",
                                    color = Color(207, 54, 66),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // 容器-全部的操作区域
                            Column(
                                verticalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                // 容器-输入框
                                Column() {
                                    // 输入框-邮箱
                                    androidx.compose.material.OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 20.dp, end = 20.dp, top = 15.dp),
                                        value = username.value,
                                        onValueChange = { username.value = it },
                                        label = {
                                            Text(
                                                text = "邮箱",
                                                color = Color.Black
                                            )
                                        },

                                        colors = if (!isErrorEmailIcon.value) TextFieldDefaults.outlinedTextFieldColors(

                                            backgroundColor = Color.White,
                                            textColor = Color.Black,
                                            leadingIconColor = Color.Black,
                                            cursorColor = Color.Black,
                                            focusedBorderColor = Color.Black,
                                            unfocusedBorderColor = Color.Gray

                                        ) else TextFieldDefaults.outlinedTextFieldColors(

                                            backgroundColor = Color.White,
                                            textColor = Color.Black,
                                            leadingIconColor = Color.Red,
                                            cursorColor = Color.Red,
                                            focusedBorderColor = Color.Red,
                                            unfocusedBorderColor = Color.Red
                                        ),

                                        leadingIcon = {

                                            IconButton(onClick = {

                                            }) {

                                                androidx.compose.material.Icon(
                                                    imageVector = Icons.Filled.Email,
                                                    contentDescription = "E-Mail Icon"
                                                )

                                            }
                                        },

                                        trailingIcon = {

                                            if (isErrorEmailIcon.value)
                                                androidx.compose.material.Icon(
                                                    Icons.Filled.Warning,
                                                    contentDescription = "E-Mail Error Icon",
                                                    tint = androidx.compose.material.MaterialTheme.colors.error
                                                )
                                        },

                                        keyboardOptions = KeyboardOptions(

                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Next

                                        )

                                    )

                                    if (isErrorEmailIcon.value) {
                                        androidx.compose.material.Text(
                                            text = isErrorEmailMessage.value,
                                            color = androidx.compose.material.MaterialTheme.colors.error,
                                            style = androidx.compose.material.MaterialTheme.typography.caption,
                                            modifier = Modifier.padding(
                                                top = 6.dp,
                                                start = 20.dp
                                            )
                                        )
                                    }

                                    // 输入框-密码
                                    androidx.compose.material.OutlinedTextField(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 20.dp, end = 20.dp, top = 15.dp),
                                        value = password.value,
                                        onValueChange = { password.value = it },
                                        label = {
                                            Text(
                                                text = "password",
                                                color = Color.Black
                                            )
                                        },

                                        colors = if (isErrorPasswordMessage.value == "Null")

                                            TextFieldDefaults.outlinedTextFieldColors(
                                                backgroundColor = Color.White,
                                                textColor = Color.Black,
                                                leadingIconColor = Color.Black,
                                                focusedBorderColor = Color.Black,
                                                unfocusedBorderColor = Color.Gray
                                            )
                                        else TextFieldDefaults.outlinedTextFieldColors(
                                            backgroundColor = Color.White,
                                            textColor = Color.Red,
                                            leadingIconColor = Color.Red,
                                            focusedBorderColor = Color.Red,
                                            unfocusedBorderColor = Color.Red
                                        ),

                                        leadingIcon = {

                                            IconButton(onClick = {

                                            }) {

                                                androidx.compose.material.Icon(
                                                    imageVector = Icons.Filled.Lock,
                                                    contentDescription = "Password İcon"
                                                )

                                            }
                                        },

                                        trailingIcon = {

                                            IconButton(onClick = {

                                                passwordVisibility = !passwordVisibility

                                            }) {

                                                androidx.compose.material.Icon(
                                                    imageVector = icon,
                                                    contentDescription = "Password İcon"
                                                )

                                            }

                                        },

                                        visualTransformation = if (passwordVisibility) VisualTransformation.None
                                        else PasswordVisualTransformation(),

                                        singleLine = true,

                                        keyboardOptions = KeyboardOptions(

                                            keyboardType = KeyboardType.Password,
                                            imeAction = ImeAction.Done,
                                        ),

                                        keyboardActions = KeyboardActions(

                                            onDone = {

                                                keyboardController?.hide()

                                            }
                                        )
                                    )
                                    // 提示-邮箱错误
                                    if (isErrorPasswordIcon.value) {
                                        androidx.compose.material.Text(
                                            text = isErrorPasswordMessage.value,
                                            color = androidx.compose.material.MaterialTheme.colors.error,
                                            style = androidx.compose.material.MaterialTheme.typography.caption,
                                            modifier = Modifier.padding(top = 5.dp, start = 20.dp)
                                        )
                                    }
                                    // 输入框扩展栏
                                    Row(
                                        modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // 复选框
                                        Checkbox(
                                            checked = checkboxDurum.value,
                                            onCheckedChange = {
                                                checkboxDurum.value = it
                                            },

                                            colors = CheckboxDefaults.colors(
                                                checkedColor = Color.Black
                                            )
                                        )
                                        // 忘记密码
                                        Text(
                                            text = "Forget Password ?",
                                            fontSize = 14.sp,
                                            modifier = Modifier
                                                .padding(start = 5.dp)
                                        )

                                    }
                                    // 登录？注册按钮
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 50.dp, end = 50.dp, top = 30.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {

                                        Button(
                                            onClick = {
                                                println("login onClick -> ${LoginUtils().loginFormatValidation(username.value,password.value)}")
                                               when(LoginUtils().loginFormatValidation(username.value,password.value)){
                                                   1 -> {
                                                       isErrorEmailIcon.value = false
                                                       scope.launch {
                                                           loginModel.getUserLogin(username.value,password.value)
                                                       }
                                                   }

                                                   2 -> {
                                                       isErrorEmailIcon.value = true
                                                       isErrorEmailMessage.value = "email is empty"
                                                   }

                                                   3 -> {
                                                       isErrorEmailIcon.value = true
                                                       isErrorEmailMessage.value = "email is too short"
                                                   }

                                                   4 -> {
                                                       isErrorEmailIcon.value = true
                                                       isErrorEmailMessage.value = " email is not contains @"
                                                   }

                                                   5 -> {
                                                       isErrorEmailIcon.value = false
                                                       isErrorEmailMessage.value = "Null"

                                                       isErrorPasswordIcon.value = true
                                                       isErrorPasswordMessage.value = "password is empty"
                                                   }


                                               }
                                            },
                                            shape = RoundedCornerShape(25.dp),
                                            modifier = Modifier
                                                .fillMaxWidth(),

                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color.Black,
                                                contentColor = Color.White
                                            )
                                        ) {
                                            Text(
                                                text = "Sign Up",
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                modifier = Modifier
                                                    .padding(top = 5.dp, bottom = 5.dp)

                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )

    }
}


@Preview
@Composable
fun DefaultPreview() {
    Login()
}