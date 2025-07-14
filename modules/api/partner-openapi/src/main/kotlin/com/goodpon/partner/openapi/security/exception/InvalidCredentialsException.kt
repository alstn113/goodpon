package com.goodpon.partner.openapi.security.exception

import org.springframework.security.core.AuthenticationException

class InvalidCredentialsException(
    message: String,
) : AuthenticationException(message)

