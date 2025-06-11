package com.goodpon.infra.security.jwt.exception

class BlankTokenException(
    cause: Throwable? = null,
) : RuntimeException("토큰이 비어있습니다.", cause)