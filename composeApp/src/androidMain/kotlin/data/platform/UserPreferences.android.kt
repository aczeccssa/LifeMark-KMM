package data.platform

import android.content.Context
import android.content.SharedPreferences
import cache.AndroidContents
import data.units.CodableException
import io.github.aakira.napier.Napier
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocalPreferences {
    actual fun putInt(key: String, value: Int) {
        LocalPreferencesHolder.editor?.putInt(key, value)
    }

    actual fun getInt(key: String, default: Int): Int {
        return LocalPreferencesHolder.sharedInstances?.getInt(key, default) ?: default
    }
    
    actual fun putString(key: String, value: String) {
        LocalPreferencesHolder.editor?.putString(key, value)?.apply()
    }
    
    actual fun getString(key: String, default: String): String {
        return LocalPreferencesHolder.sharedInstances?.getString(key, default) ?: default
    }

    actual fun putBoolean(key: String, value: Boolean) {
        LocalPreferencesHolder.editor?.putBoolean(key, value)
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return LocalPreferencesHolder.sharedInstances?.getBoolean(key, default) ?: default
    }

    actual fun putFloat(key: String, value: Float) {
        LocalPreferencesHolder.editor?.putFloat(key, value)
    }

    actual fun getFloat(key: String, default: Float): Float {
        return LocalPreferencesHolder.sharedInstances?.getFloat(key, default) ?: default
    }

    actual inline fun <reified T>putData(key: String, value: T) {
        LocalPreferencesHolder.editor?.putString(key, Json.encodeToString(value))
    }

    actual inline fun <reified T> getData(key: String): T? {
        return LocalPreferencesHolder.sharedInstances?.getString(key, null)?.let {
            Json.decodeFromString<T>(it)
        }
    }

    actual fun remove(key: String) {
        LocalPreferencesHolder.editor?.remove(key)?.commit()
    }
}

object LocalPreferencesHolder {
    private const val PREFERENCE_FILE_NAME = "shared_preference_delegate_o"

    private val context: Context?
        get() = AndroidContents.localContext

    val sharedInstances: SharedPreferences?
        get() {
            if (context === null) {
                val error = CodableException(7747, "Could not get context in LocalPreferences.")
                Napier.e("Could not get context in LocalPreferences.",error, LocalPreferences.TAG)
            }
            return context?.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
        }

    val editor: SharedPreferences.Editor?
        get() = sharedInstances?.edit()
}