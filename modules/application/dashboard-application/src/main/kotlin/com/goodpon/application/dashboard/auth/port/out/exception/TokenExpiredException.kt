package com.goodpon.application.dashboard.auth.port.out.exception

class TokenExpiredException(
    cause: Throwable? = null,
) : RuntimeException("토큰이 만료되었습니다.", cause)