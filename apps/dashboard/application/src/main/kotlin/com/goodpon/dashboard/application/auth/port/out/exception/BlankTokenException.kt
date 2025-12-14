package com.goodpon.dashboard.application.auth.port.out.exception

class BlankTokenException(
    cause: Throwable? = null,
) : RuntimeException("토큰이 비어있습니다.", cause)