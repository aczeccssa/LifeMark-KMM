package screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.models.AffiliationRegion
import data.models.CodableException
import data.models.MutableNotificationData
import data.models.UserGender
import data.models.alertNotificationModel
import data.modules.DEFAULT_AVATAR_RESOURCES_IDENTIFIER
import data.network.API
import io.github.aakira.napier.Napier
import io.ktor.http.Url
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import viewmodel.NotificationViewModel

class SignatureViewModel : ViewModel() {
    private val json = Json {
        prettyPrint = true
        isLenient = true
    }
    private val api = API()

    private val unknownRequestException = CodableException(-901, "Unknown request exception.")

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val signRes = api.postLoginEmail(email, password)
                if (signRes.main == null) throw signRes.error ?: unknownRequestException
                // @TODO: Something = signRes.main.token
                Napier.d("Token: ${signRes.main.token}")

                val userInfoRes = api.getBasicUserInfoByToken(signRes.main.token)
                if (userInfoRes.main == null) throw userInfoRes.error ?: unknownRequestException
                Napier.d(json.encodeToString(userInfoRes.main))

                val avatarUrl = Url("${API.hostname}/media/images/${userInfoRes.main.avatar}?size=large")
                NotificationViewModel.pushNotification(MutableNotificationData(
                    "Sign in successfully", "Welcome back ${userInfoRes.main.username}", avatarUrl
                ) { it() })
            } catch (e: Exception) {
                val msg = e.message ?: unknownRequestException.message
                Napier.e(msg, e)
                NotificationViewModel.pushNotification(MutableNotificationData.alertNotificationModel("Sign in failed", msg))
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                val registerRes = api.postRegisterNormalAccount(
                    email,
                    password,
                    email,
                     "",
                    UserGender.UNKNOWN,
                    AffiliationRegion.CHINA,
                    DEFAULT_AVATAR_RESOURCES_IDENTIFIER.toString()
                )
                if (registerRes.main == null) throw registerRes.error ?: unknownRequestException
                // @TODO: Save local storage = signRes.main.token
                Napier.d("Token: ${registerRes.main.token}")

                val userInfoRes = api.getBasicUserInfoByToken(registerRes.main.token)
                if (userInfoRes.main == null) throw userInfoRes.error ?: unknownRequestException
                Napier.d {
                    json.encodeToString(userInfoRes.main)
                }
                NotificationViewModel.pushNotification(MutableNotificationData("Register successfully", "Welcome ${userInfoRes.main.username}"))
            } catch (e: Exception) {
                val msg = e.message ?: unknownRequestException.message
                Napier.e(msg, e)
                NotificationViewModel.pushNotification(MutableNotificationData.alertNotificationModel("Register failed", msg))
            }
        }
    }
}