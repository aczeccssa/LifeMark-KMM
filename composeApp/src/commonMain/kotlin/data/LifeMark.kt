package data

import kotlinx.coroutines.delay

object LifeMark {
    suspend fun init() {
        delay(100)
    }
}