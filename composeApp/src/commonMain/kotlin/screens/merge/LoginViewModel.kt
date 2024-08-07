package screens.merge

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.models.PostRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel(private val postRepository: PostRepository) : ScreenModel {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    suspend fun getUserLogin(email: String, password: String) {
        println("LoginViewModel -> getUserLogin")
        if (email.trim().isEmpty() && password.trim().isEmpty()) {
            _state.value = LoginState(error = "values can't be empty", isLoading = false)
            println("LoginViewModel -> getUserLogin is empty")
            return
        }
        screenModelScope.launch {
            val flow = postRepository.getUserLogin(email, password)
            try {
                println("postRepository.getUserLogin: ${postRepository.getUserLogin(email, password)}")
                flow.collect { result ->
                    println("Received result in onEach: $result") // 打印结果
                    when (result) {
                        is Resource.Loading -> {
                            println("Loading state received.")
                            _state.value = LoginState(
                                isLoading = true,
                                internet = false
                            )
                        }

                        is Resource.Error -> {
                            println("Error state received: ${result.message}")
                            _state.value = LoginState(
                                isLoading = false,
                                internet = false,
                                error = result.message ?: "error"
                            )
                        }

                        is Resource.Internet -> {
                            println("Internet state received.")
                            delay(100)
                            _state.value = LoginState(
                                internet = true,
                                isLoading = false
                            )
                        }

                        is Resource.Success -> {
                            println("Success state received with data: ${result.data}")
                            _state.value = LoginState(
                                isLoading = false,
                                succes = 0,
                                internet = false,
                                error = "result.data.message"
                            )
                        }

                    }
                }
                _state.value = LoginState(succes = 1)
            } catch (e: Exception) {
                println("Exception caught: ${e.message}")
                _state.value = e.message?.let { LoginState(error = it) }!!
            }
        }

    }
}