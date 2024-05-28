package data.network

import com.benasher44.uuid.Uuid
import data.models.ResponseData
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import kotlinx.datetime.LocalDateTime

object Apis {
    /**
     * @Description API Services main.
     *
     * @Method GET
     *
     * @Response
     */
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
         *
         * @Response [ResponseData] main [List] type [User] Visible user list.
         */
        val LIST = Url("$HOST_NAME/user/list")

        /**
         * @Description Get one user by user id if this user is visible.
         *
         * @Method GET
         *
         * @Query id [Uuid] Number of page.
         *
         * @State 200 [Int] Success get user list.
         *
         * @State 400 [Int] Should provide user id.
         *
         * @State 404 [Int] User not found.
         *
         * @Response [ResponseData] main [User]  User which id is.
         */
        val GET_BY_ID = "$HOST_NAME/user/id/{id}"

        /**
         * @Description Search user by user keyword if this user is visible.
         *
         * @Method GET
         *
         * @Parameter keyword [String] A word of user name.
         *
         * @State 200 [Int] Success get user list.
         *
         * @State 400 [Int] Should provide user id.
         *
         * @State 404 [Int] User not found.
         *
         * @Response [ResponseData] main [List] type [User] Visible user list where conforms to keyword.
         */
        val SEARCH = "$HOST_NAME/user/search"

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
         * @State 201 [Int] User created successfully.
         *
         * @State 403 [Int] Insufficient permissions.
         *
         * @State 406 [Int] Forbidden.
         *
         * @State 408 [Int] Request timeout.
         *
         * @Response [ResponseData] main [User] Full access of your created user.
         */
        val REGISTER = "$HOST_NAME/user/register"

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
         * @State 201 [Int] Success to login.
         *
         * @State 403 [Int] Insufficient permissions.
         *
         * @State 406 [Int] Forbidden.
         *
         * @State 408 [Int] Request timeout.
         *
         * @Response [ResponseData] main [User] Full access of the user witch you already logon.
         */
        val LOGON = "$HOST_NAME/user/logon"

        /**
         * @Description Upload user avatar.
         *
         * @Method POST [HttpMethod]
         *
         * @Parameter id [Uuid] Identifier of this user.
         *
         * @State 201 [Int] Success to upload avatar for user.
         *
         * @Response [ResponseData] main [String] Message tell you successfully.
         */
        val AVATAR_UPLOAD = "$HOST_NAME/user/{id}/avarta"

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
         * @State 201 [Int] Token valid successfully.
         *
         * @State 400 [Int] Insufficient permissions.
         *
         * @State 401 [Int] Request Error.
         *
         * FIXME: @Response [ResponseData] main [String] Message tell you successfully.
         */
        val ACCOUNT_VALID = "$HOST_NAME/account/valid"
    }

    object File {

    }

    object Socialize {

        object Impression {

        }
    }

    object SessionStorage {
        /**
         * @Description Valid session storage api connection.
         *
         * @Method GET
         *
         * @State 200 [Int] Success connect to.
         *
         * @Response [ResponseData] main [String] Connection is ok.
         */
        val CONNECT = Url("$HOST_NAME/offical/sessions")

        /**
         * @Description Get how many session is visible.
         *
         * @Method GET
         *
         * @State 200 [Int] Success connect to.
         *
         * @Response [ResponseData] main [Int] Number of visible sessions.
         */
        val GET_SIZE = Url("$HOST_NAME/offical/sessions/numbers")

        /**
         * @Description To create a session into storage.
         *
         * FIXME: More descriptions for this api.
         */
        val CREATE = Url("$HOST_NAME/offical/sessions/storger/create")

        /**
         * @Description To create a session into storage.
         *
         * @Method GET
         *
         * @State 200 [Int] Success get list of visible sessions.
         *
         * @Response [ResponseData] main [List] of [Session] Number of visible sessions.
         */
        val LIST = Url("$HOST_NAME/offical/sessions/list")
    }
}