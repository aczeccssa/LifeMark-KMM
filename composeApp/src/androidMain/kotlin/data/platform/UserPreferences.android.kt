package data.platform

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import cache.AndroidContents
import data.units.CodableException
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocalPreferences {
    private val context: Context?
        get() = AndroidContents.localContext
    
    private const val PREFERENCE_FILE_NAME = "shared_preference_delegate_o"

    actual fun putInt(key: String, value: Int) {
        editor?.putInt(key, value)
    }

    actual fun getInt(key: String, default: Int): Int {
        return sharedInstances?.getInt(key, default) ?: default
    }
    
    actual fun putString(key: String, value: String) {
        editor?.putString(key, value)?.apply()
    }
    
    actual fun getString(key: String, default: String): String {
        return sharedInstances?.getString(key, default) ?: default
    }

    actual fun putBoolean(key: String, value: Boolean) {
        editor?.putBoolean(key, value)
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return sharedInstances?.getBoolean(key, default) ?: default
    }

    actual fun putFloat(key: String, value: Float) {
        editor?.putFloat(key, value)
    }

    actual fun getFloat(key: String, default: Float): Float {
        return sharedInstances?.getFloat(key, default) ?: default
    }

    actual fun putData(key: String, value: Serializable) {
        editor?.putString(key, Json.encodeToString(value))
    }

    actual fun getData(key: String): Serializable? {
        return sharedInstances?.getString(key, null)?.let {
            Json.decodeFromString<Serializable>(it)
        }
    }

    actual fun remove(key: String) {
        editor?.remove(key)?.commit()
    }
    
    private val sharedInstances: SharedPreferences?
        get() {
            if (context === null) println(CodableException(7747, "Could not get context in LocalPreferences."))
            return context?.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
        }
    
    private val editor: SharedPreferences.Editor?
        get() = sharedInstances?.edit()
}