package com.goodpon.dashboard.application.auth.port.out.exception

class InvalidTokenException(
    cause: Throwable? = null,
) : RuntimeException("유효하지 않은 토큰입니다.", cause)
