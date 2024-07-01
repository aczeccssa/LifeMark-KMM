package data.platform

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object LocalPreferences {
    fun putInt(key: String, value: Int)

    fun getInt(key: String, default: Int): Int

    fun putString(key: String, value: String)
    
    fun getString(key: String, default: String): String

    fun putBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, default: Boolean): Boolean

    fun putFloat(key: String, value: Float)

    fun getFloat(key: String, default: Float): Float

    inline fun <reified T>putData(key: String, value: T)

    inline fun <reified T> getData(key: String): T?

    fun remove(key: String)
}

val LocalPreferences.TAG: String get() = "LocalPreferences"

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object LocalPreferencesHolder