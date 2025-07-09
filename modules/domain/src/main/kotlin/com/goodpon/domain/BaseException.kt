package com.goodpon.domain

abstract class BaseException(
    message: String? = null,
    cause: Throwable? = null,
) : RuntimeException(message, cause)