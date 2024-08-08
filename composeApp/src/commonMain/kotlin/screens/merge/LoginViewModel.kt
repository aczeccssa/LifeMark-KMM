package screens.merge

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.models.PostRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(private val postRepository: PostRepository) : ScreenModel {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    suspend fun getUserLogin(email: String, password: String) {
        println("=======================LoginViewModel -> getUserLogin start=======================")
        if (email.trim().isEmpty() && password.trim().isEmpty()) {
            _state.value = LoginState(error = "values can't be empty", isLoading = false)
            println("LoginViewModel -> getUserLogin is empty")
            return
        }
        screenModelScope.launch {
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
                                error = result.message ?: "error"
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
                            println("LoginViewModel -> collect: Success ${result.data?.success}")
                            when (result.data?.success) {

                                0 -> {
                                    _state.value = LoginState(
                                        isLoading = false,
                                        success = 0,
                                        internet = false,
                                        error = result.data.message
                                    )
                                }

                                1 -> {
                                    // TODO保存登录信息到preference
                                    _state.value = LoginState(
                                        isLoading = false,
                                        internet = false,
                                        loginList = result.data.loginJSON,
                                        success = 1
                                    )
                                }

                                202 -> {
                                    _state.value = LoginState(
                                        isLoading = false,
                                        internet = false,
                                        loginList = result.data.loginJSON,
                                        success = 202
                                    )
                                }

                                203 -> {}
                            }


                        }

                    }
                }
                _state.value = LoginState(success = 1)
            } catch (e: Exception) {
                println("Exception caught: ${e.message}")
                _state.value = e.message?.let { LoginState(error = it) }!!
            }
        }

    }
}