package com.goodpon.api.dashboard.security.jwt.exception

class InvalidTokenException(
    cause: Throwable? = null,
) : RuntimeException("유효하지 않은 토큰입니다.", cause)
