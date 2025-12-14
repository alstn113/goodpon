package com.goodpon.partner.api.security.exception

import org.springframework.security.core.AuthenticationException

class ClientIdMissingException : AuthenticationException("Client ID가 누락되었습니다.")