package data.models

import data.units.CodableException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseData<T>(
    @SerialName("status") val status: Int,
    @SerialName("quota") val quota: Quota?,
    @SerialName("error") val error: CodableException?,
    @SerialName("main") @Serializable val main: T
)

@Serializable
data class TokenObject(val token: String)

@Serializable
data class Quota(val times: Int)
