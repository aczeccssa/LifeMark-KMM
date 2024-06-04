package data.platform

import platform.Foundation.NSUserDefaults

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocalPreferences {
    actual fun putString(key: String, value: String) {
        defaults.setObject(value, key)
    }
    
    actual fun getString(key: String, default: String): String {
        return defaults.stringForKey(key) ?: default
    }
    
    private val defaults: NSUserDefaults
        get() = NSUserDefaults.standardUserDefaults()
}