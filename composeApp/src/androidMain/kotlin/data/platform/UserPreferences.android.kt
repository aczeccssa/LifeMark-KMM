package data.platform

import android.content.Context
import android.content.SharedPreferences
import cache.AndroidContents

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object LocalPreferences {
    private val context: Context?
        get() = AndroidContents.localContext
    
    private const val PREFERENCE_FILE_NAME = "shared_preference_delegate_o"
    
    actual fun putString(key: String, value: String) {
        editor?.putString(key, value)?.apply()
    }
    
    actual fun getString(key: String, default: String): String {
        return sharedInstances?.getString(key, default) ?: default
    }
    
    private val sharedInstances: SharedPreferences?
        get() = context?.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
    
    private val editor: SharedPreferences.Editor?
        get() = sharedInstances?.edit()
}