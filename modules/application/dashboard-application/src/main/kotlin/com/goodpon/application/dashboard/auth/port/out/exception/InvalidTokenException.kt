package com.goodpon.application.dashboard.auth.port.out.exception

class InvalidTokenException(
    cause: Throwable? = null,
) : RuntimeException("유효하지 않은 토큰입니다.", cause)
