package cache

import com.benasher44.uuid.Uuid
import com.lestere.lifemark.kotlinmultiplatformmobile.cache.AppDatabase
import data.models.account.ActiveStatusModel
import data.models.account.GlobalAccount
import data.models.account.LevelModel
import data.models.account.UserCredentialsModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class GlobalAccountDatabase(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun getCurrentAccount(): GlobalAccount? {
        val result = dbQuery.selectOnlyOneCodableGlobalAccount().executeAsOneOrNull()
        result?.let {
            return mapAccount(
                it.id,
                it.createDate,
                it.username,
                it.email,
                it.password,
                it.status,
                it.level,
                it.impressionPoints,
                it.currentActivity
            )
        } ?: return null
    }

    private fun mapAccount(
        id: String,
        createDate: String,
        username: String,
        email: String?,
        password: String,
        status: String,
        level: Long,
        impressionPoints: Long,
        currentActivity: String?,
    ): GlobalAccount {
        return GlobalAccount(
            _id = id,
            _createDate = createDate,
            userCredentials = UserCredentialsModel(
                username = username, email = email, password = password
            ),
            activeStatus = ActiveStatusModel(Json.decodeFromString(status)),
            level = LevelModel(level.toInt(), impressionPoints.toInt()),
            _currentActivity = currentActivity
        )
    }

    internal fun insertCurrentAccount(account: GlobalAccount, token: String) {
        dbQuery.selectOnlyOneCodableGlobalAccount().executeAsOneOrNull()?.let {
            // MARK: If there is an account already, update it
            updateCurrentAccount(account)
            return
        }

        dbQuery.insertCodableGlobalAccount(
            id = account.id.toString(),
            createDate = account.createDate.toString(),
            username = account.userCredentials.username,
            email = account.userCredentials.email,
            password = account.userCredentials.password,
            status = Json.encodeToString(account.activeStatus.status),
            level = account.level.level.toLong(),
            impressionPoints = account.level.impressionPoints.toLong(),
            currentActivity = account.currentActivity.toString(),
            refreshToken = token
        )
    }

    internal fun updateCurrentAccount(account: GlobalAccount) {
        dbQuery.updateGlobalAccount(
            id = account.id.toString(),
            username = account.userCredentials.username,
            email = account.userCredentials.email,
            password = account.userCredentials.password,
            status = Json.encodeToString(account.activeStatus.status),
            level = account.level.level.toLong(),
            impressionPoints = account.level.impressionPoints.toLong(),
            currentActivity = account.currentActivity.toString(),
        )
    }

    internal fun updateRefreshToken(newToken: String, id: Uuid) {
        dbQuery.updateRefreshToken(newToken, id.toString())
    }

    internal fun deleteCurrentAccount() {
        dbQuery.removeAllCodableGlobalAccount()
    }

    internal fun selectRefreshToken(id: Uuid): String? {
        return dbQuery.selectRefreshToken(id.toString()).executeAsOneOrNull()
    }

    internal fun updatePassword(newToken: String, id: Uuid) {
        dbQuery.updateGlobalAccountPassword(newToken, id.toString())
    }
}