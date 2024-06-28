package data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseData<T>(
    @SerialName("status") val status: Int,
    @SerialName("quota") val quota: Quota?,
    @SerialName("error") val error: String?,
    @SerialName("main") @Serializable val main: T
)

@Serializable
data class Quota(val times: Int)
