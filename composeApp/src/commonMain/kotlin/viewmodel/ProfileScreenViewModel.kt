package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import data.models.account.GlobalAccount
import data.models.account.GlobalAccountManager
import data.units.now
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class ProfileScreenViewModel(private val id: Uuid = uuid4()) : ViewModel() {
    companion object {
        private const val TAG = "ProfileScreenViewModel"
    }

    init {
        Napier.i("${LocalDateTime.now()} - Account screen view model online: $id", tag = TAG)
    }

    override fun onCleared() {
        Napier.i("${LocalDateTime.now()} - Home screen view model offline: $id", tag = TAG)
        super.onCleared()
    }

    private val _accountAvatar: MutableState<String?> = mutableStateOf(null)
    val accountAvatar: MutableState<String?> get() = _accountAvatar

    val account: GlobalAccount? = GlobalAccountManager.globalAccount

    fun updateAccountAvatar() {
        viewModelScope.launch {
            try {
                _accountAvatar.value = GlobalAccountManager.getAccountAvatar()
            } catch (e: Exception) {
                // TODO: handle exception
                SnapAlertViewModel.pushSnapAlert(e.message?:"Unknown exception")
                _accountAvatar.value = null
            }
        }
    }
}