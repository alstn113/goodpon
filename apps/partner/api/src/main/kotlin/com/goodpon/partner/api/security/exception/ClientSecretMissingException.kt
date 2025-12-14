package com.goodpon.partner.api.security.exception

import org.springframework.security.core.AuthenticationException

class ClientSecretMissingException : AuthenticationException("Client Secret이 누락되었습니다.")