package data.models.account

import data.units.CodableException

enum class AccountExceptions(val exception: CodableException) {
    /**
     *
     */
    INSUFFICIENT_PERMISSIONS(CodableException(2001, "Insufficient permissions.")),
    /**
     *
     */
    FORBIDDEN(CodableException(2002, "Forbidden.")),
    /**
     *
     */
    TIMEOUT(CodableException(2003, "Request timeout.")),
    /**
     *
     */
    UNEXPECTED_ERROR(CodableException(-1001, "Unexpected error.")),
    /**
     *
     */
    NO_LOGIN_STATUS(CodableException(-1002, "Still not login account."))
}