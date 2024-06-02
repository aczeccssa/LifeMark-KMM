package data.sdk

import cache.SpaceXLaunchesDatabase
import cache.DatabaseDriverFactory
import data.entity.RocketLaunch
import data.network.SpaceXApi

/**
 * SpaceXSDK
 *
 * SpaceX SDK to interact with SpaceX API and local database.
 *
 * @param databaseDriverFactory [DatabaseDriverFactory] database driver factory.
 * @param api [SpaceXApi] spacex api.
 */
class SpaceXSDK(databaseDriverFactory: DatabaseDriverFactory, private val api: SpaceXApi) {
    // Database instance.
    private val database = SpaceXLaunchesDatabase(databaseDriverFactory)

    /**
     * Get all launched rockets
     *
     * @param forceReload [Boolean] if true will reload from network
     *
     * MARK: local cache
     *     -> empty will load on network
     *     -> forceReload will reload from network
     *     -> cache available and not forceReload will return cached data
     *
     * @throws Error Throw error when network is not available.
     */
    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllLaunchers().also {
                database.clearAndCreateLaunches(it)
            }
        }
    }
}

