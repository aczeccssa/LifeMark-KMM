package screens.merge

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.models.TokenObject
import data.units.CodableException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(private val postRepository: PostRepository) : ScreenModel {

    private val _state = mutableStateOf(LoginState<TokenObject>())
    val state: State<LoginState<TokenObject>> = _state

    suspend fun getUserLogin(email: String, password: String) {
        println("=======================LoginViewModel -> getUserLogin start=======================")
        if (email.trim().isEmpty() && password.trim().isEmpty()) {
            _state.value = LoginState(error = CodableException(-1, "Password is empty"), isLoading = false)
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



                                203 -> {}
                            }


                        }

                    }
                }
            } catch (e: Exception) {
                println("Exception caught: ${e.message}")
                _state.value = e.message?.let { LoginState(error = CodableException(-1, it)) }!!
            }
        }

    }
}