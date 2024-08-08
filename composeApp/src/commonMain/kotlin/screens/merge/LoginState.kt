package screens.merge

data class LoginState (
    val isLoading : Boolean = false,
    val success : Int = -1,
    val loginList: List<LoginModel.LoginJSON> = emptyList(),
    val error : String = "",
    val internet: Boolean = false
)