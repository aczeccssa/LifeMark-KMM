package screens.merge

class SpManager {

}

enum class Sp private constructor(val key: String) {
    USERNAME("username"),
    TOKEN("token");


    companion object {
        fun fromString(key: String): Sp? {
            return entries.firstOrNull { it.key == key }
        }
    }
}