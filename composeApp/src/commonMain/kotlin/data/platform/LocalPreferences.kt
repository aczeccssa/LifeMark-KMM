package data.platform

expect object LocalPreferences {
    fun putString(key: String, value: String)
    
    fun getString(key: String, default: String): String
}