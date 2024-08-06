package screens.merge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class LoginModel (
    @SerialName("LoginJSON")
    val loginJSON: List<LoginJSON>,
    @SerialName("message")
    val message: String,
    @SerialName("success")
    val success: Int
) {
    data class LoginJSON(
        @SerialName("Email")
        val eMail: String,
        @SerialName("AccountID")
        val accountID: String,
        @SerialName("Password")
        val password: String,
    )
}