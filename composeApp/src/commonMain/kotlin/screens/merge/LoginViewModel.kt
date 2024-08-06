package screens.merge

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.models.PostRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val postRepository: PostRepository) : ScreenModel {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    suspend fun getUserLogin(email:String, password:String) {
        if(email.trim().isEmpty() && password.trim().isEmpty()) {
            _state.value = LoginState(error = "values can't be empty", isLoading = false)

            return
        }
        screenModelScope.launch {
            try {
                _state.value = LoginState(isLoading = true)
                postRepository.loginCheck()
                _state.value = LoginState(succes = 1)
            } catch (e: Exception) {
                _state.value = e.message?.let { LoginState(error = it) }!!
            }
        }

    }
}