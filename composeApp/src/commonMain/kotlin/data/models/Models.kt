package data.models

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CodableException(val code: Int, override val message: String) : Exception(message)


@Serializable
data class SignBlindRequestData(val username: String, val password: String)

@Serializable
data class SignEmailRequestData(val email: String, val password: String)

@Serializable
data class RegisterRequestData(
    val username: String,
    val password: String,
    val email: String?,
    val bio: String,
    val gender: UserGender,
    val country: AffiliationRegion,
    val avatar: String
)

@Serializable
data class SignatureTokenResponse(val token: String)

@Serializable
data class UserBasicData(
    val id: Int,
    val username: String,
    val email: String?,
    val bio: String,
    val gender: UserGender,
    val country: String,
    @SerialName("avatar") private val _avatar: String
) {
    val avatar: Uuid get() = uuidFrom(_avatar)
}

@Serializable
enum class UserGender {
    FEMALE, MALE, UNKNOWN;

    companion object {
        fun valueOrDefaultOf(name: String): UserGender = entries.find { it.name.equals(name, true) } ?: UNKNOWN
    }
}

@Serializable
enum class AffiliationRegion {
    CHINA {
        override val location: LocationStruct = LocationStruct(34.66, 112.44)

        override val capital: String = "Beijing"
    },
    JAPAN {
        override val location: LocationStruct = LocationStruct(35.68, 139.65)

        override val capital: String = "Tokyo"
    },
    AMERICA {
        override val location: LocationStruct = LocationStruct(38.90, 77.04)

        override val capital: String = "Washington, D.C."
    },
    UK {
        override val location: LocationStruct = LocationStruct(51.51, 0.13)

        override val capital: String = "London"
    },
    FRANCE {
        override val location: LocationStruct = LocationStruct(48.86, 2.35)

        override val capital: String = "Paris"
    },
    GERMANY {
        override val location: LocationStruct = LocationStruct(52.520, 13.405)

        override val capital: String = "Berlin"
    },
    UNKNOWN {
        override val location: LocationStruct = LocationStruct(0.0, 0.0)

        override val capital: String = "Unknown"
    };

    abstract val location: LocationStruct

    abstract val capital: String
}

@Serializable
data class LocationStruct(val latitude: Double, val longitude: Double)


enum class MediaImageFileSize {
    LARGE, TINY
}
