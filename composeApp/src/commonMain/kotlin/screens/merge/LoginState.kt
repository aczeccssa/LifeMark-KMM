package screens.merge

import data.models.ResponseData
import data.units.CodableException

data class LoginState<T> (
    val isLoading : Boolean = false,
    val success : Int = -1,
    val loginList: ResponseData<T>? = null,//List<LoginModel.LoginJSON> = emptyList(),
    val error : CodableException? = null,
    val internet: Boolean = false
)