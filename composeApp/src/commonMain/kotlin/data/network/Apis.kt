package data.network

import com.benasher44.uuid.Uuid
import data.models.AffiliationRegion
import data.models.MediaImageFileSize
import data.models.RegisterRequestData
import data.models.ResponseData
import data.models.SignBlindRequestData
import data.models.SignEmailRequestData
import data.models.SignatureTokenResponse
import data.models.UserBasicData
import data.models.UserGender
import io.github.vinceglb.filekit.core.PlatformFile
import io.github.vinceglb.filekit.core.extension
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.toByteArray
import kotlinx.serialization.json.Json

class API(
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json)
        }
    }
) {
    companion object {
        private var PROTOCOL = "http"
        private var HOST: String = "127.0.0.1"
        private var PORT: Int = 8080
        val hostname: String get() = "${PROTOCOL}://${HOST}:${PORT}"

        fun updateHost(newValue: String) {
            HOST = newValue
        }
    }

    suspend fun getConnection(): ResponseData<String> = client.get(hostname).body()

    suspend fun postLoginBlind(username: String, password: String): ResponseData<SignatureTokenResponse> =
        client.post("$hostname/login/blind") {
            contentType(ContentType.Application.Json)
            setBody(SignBlindRequestData(username, password))
        }.body()

    suspend fun postLoginEmail(email: String, password: String): ResponseData<SignatureTokenResponse> =
        client.post("$hostname/login/mail") {
            contentType(ContentType.Application.Json)
            setBody(SignEmailRequestData(email, password))
        }.body()

    suspend fun getBasicUserInfoByToken(token: String): ResponseData<UserBasicData> =
        client.get("$hostname/user/information") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun postImageFileWithPair(file: PlatformFile, enablePair: Boolean = false): ResponseData<String> {
        val bytes = file.readBytes()
        return client.submitFormWithBinaryData(
            url = "${hostname}/media/images/upload?pair=${enablePair}",
            formData = formData {
                append("image", bytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/${file.extension.lowercase()}")
                    append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                })
            }).body<ResponseData<String>>()
    }

    suspend fun getImageFile(id: Uuid, size: MediaImageFileSize = MediaImageFileSize.TINY): ByteArray =
        client.get("$hostname/media/images/$id?size=${size.name}").bodyAsChannel().toByteArray()

    suspend fun postRegisterNormalAccount(
        username: String,
        password: String,
        email: String?,
        bio: String,
        gender: UserGender,
        country: AffiliationRegion,
        avatar: String
    ): ResponseData<SignatureTokenResponse> =
        client.post("${API.hostname}/registered", {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequestData(username, password, email, bio, gender, country, avatar))
        }).body<ResponseData<SignatureTokenResponse>>()
}

