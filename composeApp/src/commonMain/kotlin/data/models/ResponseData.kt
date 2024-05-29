package data.models

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import data.SpecificConfiguration
import data.currentPlatform
import data.units.now
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseData<T>(
    @SerialName("status") val status: Int,
    @SerialName("message") val message: String?,
    @SerialName("quota") val quota: Quota?,
    @SerialName("error") val error: String?,
    @SerialName("main") @Serializable val main: T
)

@Serializable
data class Quota(val times: Int)

@Serializable
data class StorageSession(
    @SerialName("id") private val _id: String,
    @SerialName("title") val title: SessionTitleBlock,
    /** Session target url */
    @SerialName("url") val url: String,
    @SerialName("code") val code: SessionCodeBlock?,
    @SerialName("description") val description: String,
    @SerialName("createDate") private val _createDate: String,
    @SerialName("updateDate") private val _updateDate: String?,
    @SerialName("expirationTime") private val _expirationTime: String?,
    @SerialName("status") val status: SessionCountStatus
) {
    val id: Uuid get() = uuidFrom(_id)

    val createDate: LocalDateTime get() = LocalDateTime.parse(_createDate)

    val updateDate: LocalDateTime? get() = _updateDate?.let { LocalDateTime.parse(it) }

    val expirationTime: LocalDateTime? get() = _expirationTime?.let { LocalDateTime.parse(it) }
}

@Serializable
data class SessionCodeBlock(val language: ProgramingLanguages, val code: String)

@Serializable
data class SessionTitleBlock(val main: String, val data: String?, val arrTag: String?)

@Serializable
enum class ProgramingLanguages {
    JAVA, CSHARP, PHP, PYTHON, OBJECTIVEC, BASH, SWIFT, C, CPP, TYPESCRIPT, JAVASCRIPT, MARKDOWN, DART,
}

@Serializable
enum class SessionCountStatus {
    ACTIVE, PENDING, EXPIRED;
}

@Serializable
data class UserAvatarResponse(@SerialName("avarta") val avatar: String)

@Serializable
open class ApiUserResponse(@SerialName("id") private val _id: String) {
    val id: Uuid get() = uuidFrom(_id)
}

@Serializable
open class ApiLocalUserResponse(private val _id: String) : ApiUserResponse(_id = _id)

enum class AccountTokenValidType {
    ACCESS, REFRESH;
}

@Serializable
data class AccountToken(val access: String, val refresh: String?) {
    @Serializable
    data class Temp(val token: String) {
        fun construct(): AccountToken {
            return AccountToken(access = token, refresh = null)
        }
    }
}

@Serializable
data class ApiNumberResponse(val number: Int)

@Serializable
data class ApiFileResponse(@SerialName("identifier") private val _identifier: String) {
    val identifier: Uuid get() = uuidFrom(_identifier)
}

@Serializable
data class ApiFileUploadBuilder(val image: String, val name: String? = null)

@Serializable
data class ApiUserLogonBuilder(
    @SerialName("username") val username: String, @SerialName("password") val password: String
) {
    @SerialName("device")
    val device: String get() = SpecificConfiguration.currentPlatform.name

    @SerialName("timestamp")
    val timestamp: String = LocalDateTime.now().toString()
}
