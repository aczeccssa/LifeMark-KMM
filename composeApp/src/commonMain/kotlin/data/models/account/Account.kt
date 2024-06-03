package data.models.account

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cache.DatabaseDriverFactory
import cache.GlobalAccountDatabase
import cache.SpaceXLaunchesDatabase
import data.models.ApiUserLogonBuilder
import data.network.Apis
import data.units.CodableException
import data.units.now
import io.kamel.core.utils.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.datetime.LocalDateTime
import kotlin.coroutines.cancellation.CancellationException

object GlobalAccountManager {
    private val database = GlobalAccountDatabase(DatabaseDriverFactory())

    init {
        println("${LocalDateTime.now()} - Notification view model online(Global manager)")
        // TODO: Get account from database.
        // TODO: Get refresh token from database.
        database.getCurrentAccount()?.let {
            // TODO: Get account from database.
            updateStateAccount(it)
        }
    }

    // Coroutine Scope for this manager only.
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _globalAccount: MutableState<GlobalAccountState?> = mutableStateOf(null)

    val globalAccount: GlobalAccount? get() = _globalAccount.value?.account

    private val refreshToken: String? get() = _globalAccount.value?.refreshToken

    /**
     *
     */
    @Throws(CodableException::class, CancellationException::class)
    suspend fun logon(username: String, password: String) {
        val builder = ApiUserLogonBuilder(username, password)
        try {
            val result = Apis.User.logon(builder)
            // TODO: Progress error
            when(result.status) {
                403 -> { throw AccountExceptions.INSUFFICIENT_PERMISSIONS.exception }
                406 -> { throw AccountExceptions.FORBIDDEN.exception }
                408 -> { throw AccountExceptions.TIMEOUT.exception }
            }

            // TODO: Update account like login or register implementation.
            updateStateAccount(result.main.account)
            updateStateRefreshToken(result.main.refreshToken)
        } catch (e: CodableException) {
            println("Error: $e")
            throw e
        } catch (e: Error) {
            println("Error: $e")
            throw AccountExceptions.UNEXPECTED_ERROR.exception
        }
    }

    /**
     *
     */
    @Throws(CodableException::class, CancellationException::class)
    suspend fun register(username: String, password: String) {
        val builder = ApiUserLogonBuilder(username, password)
        try {
            val result = Apis.User.register(builder)
            // TODO: Progress error
            when(result.status) {
                403 -> { throw AccountExceptions.INSUFFICIENT_PERMISSIONS.exception }
                406 -> { throw AccountExceptions.FORBIDDEN.exception }
                408 -> { throw AccountExceptions.TIMEOUT.exception }
            }

            // TODO: Update account like login or register implementation.
            updateStateAccount(result.main.account)
            updateStateRefreshToken(result.main.refreshToken)
        } catch (e: CodableException) {
            println("Error: $e")
            throw e
        } catch (e: Error) {
            println("Error: $e")
            throw AccountExceptions.UNEXPECTED_ERROR.exception
        }
    }

    /**
     *
     */
    fun logout() {
        _globalAccount.value = null
        database.deleteCurrentAccount()
    }

    /**
     *
     */
    @Throws(CodableException::class, CancellationException::class)
    suspend fun getAccountAvatar(): String {
        _globalAccount.value?.let {
            try {
                val result = Apis.User.getUserAvatar(it.account.id)
                return result.main.avatar
            } catch (e: Exception) {
                throw AccountExceptions.UNEXPECTED_ERROR.exception
            } catch (e: CodableException) {
                println("${LocalDateTime.now()} - Error: ${e.message}")
                throw e
            }
        }?: throw AccountExceptions.NO_LOGIN_STATUS.exception
    }

    private fun updateStateAccount(account: GlobalAccount) {
        database.updateCurrentAccount(account)
        _globalAccount.value = _globalAccount.value?.copy(account = account)
    }

    @Throws(CodableException::class, CancellationException::class)
    private fun updateStateRefreshToken(token: String) {
        _globalAccount.value?.let {
            database.updateRefreshToken(token, it.account.id)
            _globalAccount.value = _globalAccount.value?.copy(refreshToken = token)
        }?: throw AccountExceptions.NO_LOGIN_STATUS.exception
    }

    @Throws(CodableException::class, CancellationException::class)
    private fun getAccountRefreshToken(): String {
        _globalAccount.value?.let {
            database.selectRefreshToken(it.account.id)?.let {
                return it
            }?: throw AccountExceptions.NO_LOGIN_STATUS.exception
        }?: throw AccountExceptions.NO_LOGIN_STATUS.exception
    }
}

data class GlobalAccountState(val account: GlobalAccount, val refreshToken: String)