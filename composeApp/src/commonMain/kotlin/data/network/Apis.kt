package data.network

import app.cash.sqldelight.Query
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.Url
import kotlinx.serialization.Serializable

object Apis {
    val HOST_NAME = Url("http://120.79.35.203:3000")

    object User {
        /**
         * @Description Get all users list if user is visible.
         *
         * @Method GET
         *
         * @Query page [Int] Number of page
         *
         * @State 200 [Int] Success get user list
         */
        val USER_LIST = Url(HOST_NAME.host + "/user/list")

        val USER_BY_ID = Url(HOST_NAME.host + "/user/id/{id}")
    }
}

//annotation class APIRequester(
//    val host: String,
//    val method: HttpMethod,
//    val query: List<HttpQuery<Serializable>>,
//    val parameters: Parameters,
//)
//
//@Serializable
//data class HttpQuery<T: Serializable>(
//    val query: String,
//    val value: T
//)