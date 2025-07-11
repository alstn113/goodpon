package com.goodpon.dashboard.api.security.exception

import org.springframework.security.access.AccessDeniedException

class AccountNotVerifiedException(
    throwable: Throwable? = null,
) : AccessDeniedException("계정이 아직 인증되지 않았습니다.", throwable)