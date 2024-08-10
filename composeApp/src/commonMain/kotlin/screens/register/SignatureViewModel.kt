package screens.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.screenModelScope
import data.models.MutableNotificationData
import data.models.PostRepository
import data.models.TokenObject
import data.units.CodableException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import screens.merge.LoginState
import screens.merge.Resource
import viewmodel.NotificationViewModel

class SignatureViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _state = mutableStateOf(LoginState<TokenObject?>())
    val state: State<LoginState<TokenObject?>> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            NotificationViewModel.pushNotification(MutableNotificationData("小恶魔捏", "$email 想要登陆但是我拒绝了嘻嘻!", null) { })

            val flow = postRepository.getUserLogin(email, password)
            try {
                flow.collect { result ->
                    println("LoginViewModel -> getUserLogin -> getUserLogin.collect: $result") // 打印结果
                    when (result) {
                        is Resource.Loading -> {
                            println("LoginViewModel -> collect: Loading")
                            _state.value = LoginState(
                                isLoading = true,
                                internet = false
                            )
                        }

                        is Resource.Error -> {
                            println("LoginViewModel -> collect: Error ${result.message}")
                            _state.value = LoginState(
                                isLoading = false,
                                internet = false,
                                error = result.data?.error
                            )
                        }

                        is Resource.Internet -> {
                            println("LoginViewModel -> collect: Error ${result.message}")
                            delay(100)
                            _state.value = LoginState(
                                internet = true,
                                isLoading = false
                            )
                        }

                        is Resource.Success -> {
                            println("LoginViewModel -> collect: Success ${result.data?.status}")
                            when (result.data?.status) {

                                0 -> {
                                    _state.value = LoginState(
                                        isLoading = false,
                                        success = 0,
                                        internet = false,
                                        error = result.data.error
                                    )
                                }

                                200 -> {
                                    // TODO保存登录信息到preference
                                    _state.value = LoginState(
                                        isLoading = false,
                                        internet = false,
                                        loginList = result.data,
                                        success = 200
                                    )
                                }

                                400 -> {
                                    _state.value = LoginState(
                                        isLoading = false,
                                        internet = false,
                                        loginList = result.data,
                                        success = 400
                                    )
                                }

                                203 -> {}
                            }


                        }

                    }
                }
            } catch (e: Exception) {
                println("Exception caught: ${e.message}")
                _state.value = e.message?.let { LoginState(error = CodableException(1001, it)) }!!
            }
        }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            if (password == confirmPassword) {
                NotificationViewModel.pushNotification(MutableNotificationData("小恶魔捏", "$email 想要注册但是我拒绝了嘻嘻!", null) { })
            } else {
                NotificationViewModel.pushNotification(MutableNotificationData("小恶魔捏", "密码不一样不给你注册!", null) { })
            }

            }

        }

}