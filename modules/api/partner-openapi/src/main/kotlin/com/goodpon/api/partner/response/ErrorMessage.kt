package com.goodpon.api.partner.response

data class ErrorMessage private constructor(
    val code: String,
    val message: String,
    val data: Any? = null,
) {
    constructor(errorType: ErrorType, data: Any? = null) : this(
        code = errorType.name,
        message = errorType.message,
        data = data,
    )
}
