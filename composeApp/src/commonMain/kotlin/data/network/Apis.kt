package data.network

import com.benasher44.uuid.Uuid
import data.models.AccountToken
import data.models.AccountTokenValidType
import data.models.ApiFileResponse
import data.models.ApiFileUploadBuilder
import data.models.ApiNumberResponse
import data.models.ResponseData
import data.models.StorageSession
import data.models.ApiLocalUserResponse
import data.models.ApiUserResponse
import data.models.UserAvatarResponse
import data.models.ApiUserLogonBuilder
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

object Apis {
    /** HTTP client for making requests. */
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    val HOST_NAME = Url("http://120.79.35.203:3000")

    /**
     * @Description API Services main.
     *
     * @Method GET
     *
     * @Status 200 [Int] Success connect with server.
     *
     * @Status 404 [Int] Server already down.
     *
     * @Response [ResponseData] main [String] Welcome message form server.
     */
    suspend fun getServerConnection(): ResponseData<String> = httpClient.get(HOST_NAME).body()

    object User {
        /**
         * @Description Get all users list if user is visible.
         *
         * @Method GET
         *
         * @Query page [Int] Number of page
         *
         * @Status 200 [Int] Success get user list
         *
         * @Response [ResponseData] main [List] type [ApiUserResponse] Visible user list.
         */
        suspend fun getUserList(page: Int = 1): ResponseData<List<ApiUserResponse>> {
            return httpClient.get("$HOST_NAME/user/list?page=${page}").body()
        }

        /**
         * @Description Get one user by user id if this user is visible.
         *
         * @Method GET
         *
         * @Query id [Uuid] Number of page.
         *
         * @Status 200 [Int] Success get user list.
         *
         * @Status 400 [Int] Should provide user id.
         *
         * @Status 404 [Int] User not found.
         *
         * @Response [ResponseData] main [ApiUserResponse]  User which id is.
         */
        suspend fun getUserById(id: Uuid): ResponseData<ApiUserResponse> {
            return httpClient.get("$HOST_NAME/user/id/${id}").body()
        }

        /**
         * @Description Search user by user keyword if this user is visible.
         *
         * @Method GET
         *
         * @Parameter keyword [String] A word of user name.
         *
         * @Status 200 [Int] Success get user list.
         *
         * @Status 400 [Int] Should provide user id.
         *
         * @Status 404 [Int] User not found.
         *
         * @Response [ResponseData] main [List] type [ApiUserResponse] Visible user list where conforms to keyword.
         */
        suspend fun getUsersByKeyword(keyword: String): ResponseData<List<ApiUserResponse>> {
            return httpClient.get("$HOST_NAME/user/search?keyword=${keyword}").body()
        }

        /**
         * @Description Register to.
         *
         * @Method POST [HttpMethod]
         *
         * @Parameter username [String] Name of this user.
         *
         * @Parameter password [String] Password of this user.
         *
         * @Parameter device [String] Request type of current device.
         *
         * @Parameter timestamp [LocalDateTime] Current request time.
         *
         * @Status 201 [Int] User created successfully.
         *
         * @Status 403 [Int] Insufficient permissions.
         *
         * @Status 406 [Int] Forbidden.
         *
         * @Status 408 [Int] Request timeout.
         *
         * @Response [ResponseData] main [ApiLocalUserResponse] Full access of your created user.
         */
        @OptIn(InternalAPI::class)
        suspend fun register(parameters: ApiUserLogonBuilder): ResponseData<ApiLocalUserResponse> {
            return httpClient.post("$HOST_NAME/user/register") { body = parameters }.body()
        }

        /**
         * @Description Logon to.
         *
         * @Method POST [HttpMethod]
         *
         * @Parameter username [String] Name of this user.
         *
         * @Parameter password [String] Password of this user.
         *
         * @Parameter device [String] Request type of current device.
         *
         * @Parameter timestamp [LocalDateTime] Current request time.
         *
         * @Status 201 [Int] Success to login.
         *
         * @Status 403 [Int] Insufficient permissions.
         *
         * @Status 406 [Int] Forbidden.
         *
         * @Status 408 [Int] Request timeout.
         *
         * @Response [ResponseData] main [User] Full access of the user witch you already logon.
         */
        @OptIn(InternalAPI::class)
        suspend fun logon(parameters: ApiUserLogonBuilder): ResponseData<ApiLocalUserResponse> {
            return httpClient.post("$HOST_NAME/user/logon") { body = parameters }.body()
        }

        /**
         * @Description Upload user avatar.
         *
         * @Method POST [HttpMethod]
         *
         * @Header Authorization [String] Token of this user.
         *
         * @Query id [Uuid] Identifier of this user.
         *
         * @Parameter fileIdentifier [Uuid] Identifier of this file.
         *
         * @Status 200 [Int] Upload avatar successfully.
         *
         * @Status 400 [Int] Request missing fileIdentifier.
         *
         * @Status 401 [Int] Request Unauthorized.
         *
         * @Response [ResponseData] main [String] Message tell you successfully.
         */
        @OptIn(InternalAPI::class)
        suspend fun uploadAvatar(
            userId: Uuid, token: String, avatarId: Uuid
        ): ResponseData<String> {
            return httpClient.post("$HOST_NAME/user/${userId}/avarta/upload") {
                headers.append("Authorization", token)
                body = "{\"fileIdentifier\":\"$avatarId\"}"
            }.body()
        }

        /**
         * @Description Upload user avatar.
         *
         * @Method POST [HttpMethod]
         *
         * @Parameter id [Uuid] Identifier of this user.
         *
         * @Status 201 [Int] Success to upload avatar for user.
         *
         * @Response [ResponseData] main [String] Message tell you successfully.
         */
        suspend fun getUserAvatar(id: Uuid): ResponseData<UserAvatarResponse> {
            return httpClient.get("$HOST_NAME/user/${id}/avarta").body()
        }

        /**
         * @Description Upload user avatar.
         *
         * @Method POST [HttpMethod]
         *
         * @Header Authorization [String] User token.
         *
         * @Query type [String] Type witch wanna to authorize.
         *
         * @Parameter id [Uuid] Identifier of this user.
         *
         * @Status 201 [Int] Token valid successfully.
         *
         * @Status 400 [Int] Insufficient permissions.
         *
         * @Status 401 [Int] Request Error.
         *
         * FIXME: @Response [ResponseData] main [String] Message tell you successfully.
         */
        suspend fun validateToken(
            id: Uuid, token: String, type: AccountTokenValidType
        ): ResponseData<AccountToken> {
            val response = httpClient.post("$HOST_NAME/account/valid?type=${type.name}") {
                headers.append("Authorization", token)
            }

            return when (type) {
                AccountTokenValidType.ACCESS -> {

                    val temp = response.body<ResponseData<AccountToken.Temp>>()
                    return ResponseData(
                        status = temp.status,
                        message = temp.message,
                        quota = temp.quota,
                        error = temp.error,
                        main = temp.main.construct()
                    )
                }

                AccountTokenValidType.REFRESH -> response.body<ResponseData<AccountToken>>()
            }
        }
    }

    object FileStorage {
        /**
         * @Description Upload file.
         *
         * @Method POST [HttpMethod]
         *
         * @Header Authorization [String] User token.
         *
         * @Parameter data [ApiFileUploadBuilder] File that wanna to upload.
         *
         * @Header Authorization [String] Indeed when file upload by personal.
         *
         * @Status 201 [Int] Success to upload and create file.
         *
         * @Status 400 [Int] File storage has wrong.
         *
         * @Status 500 [Int] Something went wrong.
         *
         * @Response [ResponseData] main [ApiFileResponse] Response of upload file identifier.
         */
        @OptIn(InternalAPI::class)
        suspend fun upload(data: ApiFileUploadBuilder, token: String? = null): ResponseData<ApiFileResponse> {
            return httpClient.post("$HOST_NAME/file/upload"){
                headers.append("Authorization", token ?: "")
                body = data
            }.body()
        }

        /**
         * @Description Get file.
         *
         * @Method GET [HttpMethod]
         *
         * @Query id [Uuid] Identifier of this file.
         *
         * @Status 200 [Int] Success to get file.
         *
         * @Status 404 [Int] Image not found.
         *
         * @Response [ResponseData] main [ApiFileResponse] Response of get file.
         */
        suspend fun get(id: Uuid): ResponseData<ApiFileResponse> {
            return httpClient.get("$HOST_NAME/files/media/${id}").body()
        }

        /**
         * @Description Get file list.
         *
         * @Method GET [HttpMethod]
         *
         * @Status 200 [Int] Success to get file list.
         *
         * @Status 500 [Int] Something went wrong.
         *
         * @Response [ResponseData] main [List] of [String] List of file identifier.
         */
        suspend fun list(): ResponseData<List<String>> {
            return httpClient.get("$HOST_NAME/files/list").body()
        }
    }

    object SessionStorage {
        /**
         * @Description Valid session storage api connection.
         *
         * @Method GET
         *
         * @Status 200 [Int] Success connect to.
         *
         * @Response [ResponseData] main [String] Connection is ok.
         */
        suspend fun connect(): ResponseData<String> {
            return httpClient.get("$HOST_NAME/offical/sessions").body()
        }

        /**
         * @Description Get how many session is visible.
         *
         * @Method GET
         *
         * @Status 200 [Int] Success connect to.
         *
         * @Response [ResponseData] main [ApiNumberResponse] Number of visible sessions.
         */
        suspend fun getSizeOfSessions(): ResponseData<ApiNumberResponse> {
            return httpClient.get("$HOST_NAME/offical/sessions").body()
        }

        /**
         * @Description To create a session into storage.
         *
         * @Method POST
         *
         * @Parameter [StorageSession] Source data for update.
         *
         * @Status 201 [Int] Success to create new session.
         *
         * @Status 400 [Int] Unexpected error.
         *
         * @Response [ResponseData] main [StorageSession] The response of created session.
         */
        @OptIn(InternalAPI::class)
        suspend fun createSession(source: StorageSession): ResponseData<StorageSession> {
            return httpClient.post("$HOST_NAME/offical/sessions/storger/create") { body = source }
                .body()
        }

        /**
         * @Description To create a session into storage.
         *
         * @Method GET
         *
         * @Status 200 [Int] Success get list of visible sessions.
         *
         * @Response [ResponseData] main [List] of [StorageSession] Number of visible sessions.
         */
        suspend fun getSessionsList(): ResponseData<List<StorageSession>> {
            return httpClient.get("$HOST_NAME/offical/sessions/list").body()
        }
    }
}

