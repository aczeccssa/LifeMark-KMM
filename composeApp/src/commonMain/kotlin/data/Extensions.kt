package data

fun Boolean.toggle(): Boolean {
    return !this
}

fun String.toInt(default: Int): Int {
    return try {
        this.toInt()
    } catch (exception: Exception) {
        println(exception)
        default
    } catch (error: Error) {
        println(error)
        default
    }
}
