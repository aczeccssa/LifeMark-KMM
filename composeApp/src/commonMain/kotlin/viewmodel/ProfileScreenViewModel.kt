package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4
import data.models.SnapAlertData
import data.models.account.GlobalAccount
import data.models.account.GlobalAccountManager
import data.units.now
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

class ProfileScreenViewModel(private val id: Uuid = uuid4()) : ViewModel() {
    init {
        println("${LocalDateTime.now()} - Account screen view model online: $id")
    }

    override fun onCleared() {
        println("${LocalDateTime.now()} - Home screen view model offline: $id")
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
                SnapAlertViewModel.pushSnapAlert(SnapAlertData(e.message?:"Unknown exception"))
                _accountAvatar.value = null
            }
        }
    }
}