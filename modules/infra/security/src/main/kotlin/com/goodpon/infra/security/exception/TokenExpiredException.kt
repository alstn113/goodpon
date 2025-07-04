package com.goodpon.infra.security.exception

class TokenExpiredException(
    cause: Throwable? = null,
) : RuntimeException("토큰이 만료되었습니다.", cause)