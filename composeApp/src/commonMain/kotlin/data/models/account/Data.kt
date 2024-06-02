package data.models.account

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GlobalAccount(
    @SerialName("id")
    private val _id: String,
    @SerialName("createDate")
    private val _createDate: String,
    // MARK: val sobriquet: Sobriquet, [ Expired feature ]
    @SerialName("userCredentials")
    val userCredentials: UserCredentialsModel,
    @SerialName("activeStatus")
    val activeStatus: ActiveStatusModel,
    @SerialName("level")
    val level: LevelModel,
    @SerialName("currentActivity")
    private val _currentActivity: String? // Uuid
) {
    val id: Uuid get() = uuidFrom(this._id)

    val createDate: LocalDateTime get() = LocalDateTime.parse(this._createDate)

    val currentActivity: Uuid? get() = this._currentActivity?.let { uuidFrom(it) }
}


///**
// * enum SobriquetModule: Codable {
// *     case _0001, _0002, _0003, custom(String)
// *
// *     var sobriquetMain: String {
// *         switch self {
// *         case ._0001: return "新来的"
// *         case ._0002: return "荔枝"
// *         case ._0003: return "ヾ(≧▽≦*)o"
// *         case .custom(let detail):
// *             return detail
// *         }
// *     }
// * }
// */
//@Serializable
//class Sobriquet(val sobriquetMain: String) {
//    companion object {
//        @SerialName("_0001")
//        val OPTION_1 = Sobriquet("新来的")
//        @SerialName("_0002")
//        val OPTION_2 = Sobriquet("荔枝")
//        @SerialName("_0003")
//        val OPTION_3 = Sobriquet("ヾ(≧▽≦*)o")
//
//        // Custom option
//        fun custom(detail: String) = Sobriquet(detail)
//    }
//}


@Serializable
data class UserCredentialsModel(
    @SerialName("username") val username: String,
    @SerialName("email") val email: String?,
    @SerialName("password") val password: String
)

@Serializable
data class ActiveStatusModel(
    @SerialName("status") val status: AccountStatus
)

@Serializable
data class LevelModel(
    @SerialName("level") val level: Int,
    @SerialName("impressionPoints") val impressionPoints: Int
)

@Serializable
enum class AccountStatus {
    @SerialName("isActive")
    ACTIVE,
    @SerialName("isLocked")
    LOCKED;
}


//export type ExecutableUserModel = {
//    id: string;
//    sobriquet: string;
//    username: string;
//    level: LevelModel;
//    currentActivity: string | null;
//}