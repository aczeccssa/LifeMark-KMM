package data.platform

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import platform.Foundation.NSUserDefaults

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocalPreferences {
    actual fun putInt(key: String, value: Int) {
        LocalPreferencesHolder.defaults.setInteger(value.toLong(), key)
    }

    actual fun getInt(key: String, default: Int): Int {
        return LocalPreferencesHolder.defaults.integerForKey(key)?.toInt() ?: default
    }

    actual fun putString(key: String, value: String) {
        LocalPreferencesHolder.defaults.setObject(value, key)
    }

    actual fun getString(key: String, default: String): String {
        return LocalPreferencesHolder.defaults.stringForKey(key) ?: default
    }

    actual fun putBoolean(key: String, value: Boolean) {
        LocalPreferencesHolder.defaults.setBool(value, key)
    }

    actual fun getBoolean(key: String, default: Boolean): Boolean {
        return LocalPreferencesHolder.defaults.boolForKey(key) ?: default
    }

    actual fun putFloat(key: String, value: Float) {
        LocalPreferencesHolder.defaults.setFloat(value, key)
    }

    actual fun getFloat(key: String, default: Float): Float {
        return LocalPreferencesHolder.defaults.floatForKey(key) ?: default
    }

    actual inline fun <reified T> putData(key: String, value: T) {
        LocalPreferencesHolder.defaults.setObject(key, Json.encodeToString(value))
    }

    actual inline fun <reified T> getData(key: String): T? {
        return LocalPreferencesHolder.defaults.stringForKey(key)?.let {
            Json.decodeFromString<T>(it)
        }
    }

    actual fun remove(key: String) {
        LocalPreferencesHolder.defaults.removeObjectForKey(key)
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocalPreferencesHolder {
    val defaults: NSUserDefaults
        get() = NSUserDefaults.standardUserDefaults()
}