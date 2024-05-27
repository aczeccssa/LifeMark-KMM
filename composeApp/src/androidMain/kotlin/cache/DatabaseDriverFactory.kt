package cache

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.lestere.lifemark.kotlinmultiplatformmobile.cache.AppDatabase
import java.lang.annotation.Native

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