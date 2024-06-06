package data.platform

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import platform.Foundation.NSUserDefaults

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocalPreferences {
    actual fun putInt(key: String, value: Int) {
        defaults.setInteger(value.toLong(), key)
    }

    actual fun getInt(key: String, default: Int): Int {
        return defaults.integerForKey(key)?.toInt() ?: default
    }

    actual fun putString(key: String, value: String) {
        defaults.setObject(value, key)
    }
    
    actual fun getString(key: String, default: String): String {
        return defaults.stringForKey(key) ?: default
    }

    actual fun putBoolean(key: String, value: Boolean) {
        defaults.setBool(value, key)
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return defaults.boolForKey(key) ?: default
    }

    actual fun putFloat(key: String, value: Float) {
        defaults.setFloat(value, key)
    }

    actual fun getFloat(key: String, default: Float): Float {
        return defaults.floatForKey(key) ?: default
    }

    actual fun putData(key: String, value: Serializable) {
        defaults.setObject(key, Json.encodeToString(value))
    }

    actual fun getData(key: String): Serializable? {
        return defaults.objectForKey(key)?.let {
            if (it is String) Json.decodeFromString<Serializable>(it) else null
        } ?: null
    }

    actual fun remove(key: String) {
        defaults.removeObjectForKey(key)
    }

    private val defaults: NSUserDefaults
        get() = NSUserDefaults.standardUserDefaults()
}