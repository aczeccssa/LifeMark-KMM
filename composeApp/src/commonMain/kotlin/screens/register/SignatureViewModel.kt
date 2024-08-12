package screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.models.MutableNotificationData
import kotlinx.coroutines.launch
import viewmodel.NotificationViewModel

class SignatureViewModel : ViewModel() {
    fun login(email: String, password: String) {
        viewModelScope.launch {
            NotificationViewModel.pushNotification(MutableNotificationData("小恶魔捏", "$email 想要登陆但是我拒绝了嘻嘻!", null) { })
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            NotificationViewModel.pushNotification(MutableNotificationData("小恶魔捏", "$email 想要注册但是我拒绝了嘻嘻!", null) { })
        }
    }
}