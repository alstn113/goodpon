package com.goodpon.domain.support.error

abstract class BaseException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)