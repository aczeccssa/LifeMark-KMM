package data.platform

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocalPreferences {
    actual fun putInt(key: String, value: Int) {
        LocalPreferencesDefaultsHolder.defaults.setInteger(value.toLong(), key)
    }

    actual fun getInt(key: String, default: Int): Int {
        return LocalPreferencesDefaultsHolder.defaults.integerForKey(key)?.toInt() ?: default
    }

    actual fun putString(key: String, value: String) {
        LocalPreferencesDefaultsHolder.defaults.setObject(value, key)
    }

    actual fun getString(key: String, default: String): String {
        return LocalPreferencesDefaultsHolder.defaults.stringForKey(key) ?: default
    }

    actual fun putBoolean(key: String, value: Boolean) {
        LocalPreferencesDefaultsHolder.defaults.setBool(value, key)
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return LocalPreferencesDefaultsHolder.defaults.boolForKey(key) ?: default
    }

    actual fun putFloat(key: String, value: Float) {
        LocalPreferencesDefaultsHolder.defaults.setFloat(value, key)
    }

    actual fun getFloat(key: String, default: Float): Float {
        return LocalPreferencesDefaultsHolder.defaults.floatForKey(key) ?: default
    }

    actual inline fun <reified T> putData(key: String, value: T) {
        LocalPreferencesDefaultsHolder.defaults.setObject(key, Json.encodeToString(value))
    }

    actual inline fun <reified T> getData(key: String): T? {
        return LocalPreferencesDefaultsHolder.defaults.stringForKey(key)?.let {
            Json.decodeFromString<T>(it)
        }
    }

    actual fun remove(key: String) {
        LocalPreferencesDefaultsHolder.defaults.removeObjectForKey(key)
    }
}

object LocalPreferencesDefaultsHolder {
    val defaults: NSUserDefaults
        get() = NSUserDefaults.standardUserDefaults()
}