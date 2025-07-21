package com.goodpon.api.dashboard.security.exception

import org.springframework.security.authorization.AuthorizationDeniedException

class AccountNotVerifiedException : AuthorizationDeniedException("계정이 아직 인증되지 않았습니다.")