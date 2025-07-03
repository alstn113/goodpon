package com.goodpon.core.support.error

abstract class BaseException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)