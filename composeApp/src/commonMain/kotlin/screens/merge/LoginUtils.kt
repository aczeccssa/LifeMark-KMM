package screens.merge

class LoginUtils {
    /**
     * 2 -> email is empty
     * 3 -> email is too short
     * 4 -> email is not contains @
     * 5 -> password is empty
     * 1 -> all is ok
     */
    fun loginFormatValidation(email:String, password:String):Int {
        if (email.trim().isNotEmpty()) {

            return if (email.length > 5) {

                if (email.contains("@")) {

                    if (password.trim().isNotEmpty()) {

                        1

                    } else {

                        5

                    }

                } else {

                    4

                }

            } else {

                3

            }

        } else {

            return 2

        }
    }
}