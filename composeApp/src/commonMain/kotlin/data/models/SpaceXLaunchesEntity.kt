package data.models

import com.usecase.picture_selector.Media
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketLaunch(
    @SerialName("flight_number")
    val flightNumber: Int,
    @SerialName("name")
    val missionName: String,
    @SerialName("date_utc")
    val launchDateUTC: String,
    @SerialName("details")
    val details: String?,
    @SerialName("success")
    val launchSuccess: Boolean?,
    @SerialName("links")
    val links: Links
) {
    var launchYear = Instant.parse(launchDateUTC).toLocalDateTime(TimeZone.UTC).year
}

@Serializable
data class Links(
    @SerialName("patch")
    val patch: Patch?,
    @SerialName("article")
    val article: String?
)

@Serializable
data class Patch(
    @SerialName("small")
    val small: String?,
    @SerialName("large")
    val large: String?
)

data class Post(
    val title: String?,
    val description: String?,
    val files: List<Media?>
)

@Serializable
data class PostObject (
    // 没有数据库，临时屏蔽id，内容设置可空
    var id:Int? = 0,
    val title:String?,
    val content:String?,
    val imageUrl:List<String?>,
)

@Serializable
data class PhotoObject(
    val objectID: Int,
    val title: String,
    val artistDisplayName: String,
    val medium: String,
    val dimensions: String,
    val objectURL: String,
    val objectDate: String,
    val primaryImage: String,
    val primaryImageSmall: String,
    val repository: String,
    val department: String,
    val creditLine: String,
)

@Serializable
data class AccountLoginEmailStruct(val email: String, val password: String)

@Serializable
data class AccountRegisteredStruct(
    val username: String,
    val email: String?,
    val bio: String?,
    @SerialName("password") private val _password: String,
    @SerialName("gender") private val _gender: String,
    @SerialName("country") private val _country: String,
    @SerialName("avatar") private val _avatar: String
)