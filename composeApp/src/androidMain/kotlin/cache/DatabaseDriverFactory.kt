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
    var localContext: Context? = null
}