package com.goodpon.domain.support.error

data class ErrorMessage private constructor(
    val code: String,
    val message: String,
) {

    constructor(errorType: ErrorType) : this(
        code = errorType.name,
        message = errorType.message,
    )
}
