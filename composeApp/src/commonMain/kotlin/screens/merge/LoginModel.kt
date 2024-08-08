package screens.merge

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginModel (
    @SerialName("LoginJSON")
    val loginJSON: List<LoginJSON>,
    @SerialName("message")
    val message: String,
    @SerialName("success")
    val success: Int
) {
    @Serializable
    data class LoginJSON(
        @SerialName("email")
        val email: String,
        @SerialName("accountID")
        val accountID: String,
    )
}