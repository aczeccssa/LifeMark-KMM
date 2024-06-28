package cache

import android.annotation.SuppressLint
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.lestere.lifemark.kotlinmultiplatformmobile.cache.AppDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DatabaseDriverFactory {
    private val context: Context = AndroidContents.localContext!!

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "launch.db")
    }
}

@SuppressLint("StaticFieldLeak")
object AndroidContents {
    // This is a singleton, so we can cache the context.
    // This is safe because we don't access the context from any other threads.
    // We only access it from the main thread.
    // We don't wish set null to this variable.
    // MARK: Honestly this is not a good solution...
    var localContext: Context? = null
        set(value) {
            value?.also { field = it }
        }
}