package cache

import androidx.compose.runtime.Composable
import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {
    fun createDriver(): SqlDriver
}