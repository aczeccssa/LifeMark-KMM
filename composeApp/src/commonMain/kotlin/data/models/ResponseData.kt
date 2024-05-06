package data.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ResponseData<T>(
    val status: Int,
    val message: String?,
    val quota: Quota?,
    val error: String?,
    @Serializable val main: T
)

@Serializable
data class Quota(
    val times: Int
)