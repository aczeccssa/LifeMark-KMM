package data.network

import data.models.ResponseData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
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

    val HOST_NAME = Url("http://localhost:8080")

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
}

