package com.goodpon.partner.api.security.exception

import org.springframework.security.core.AuthenticationException

class InvalidCredentialsException(
    message: String,
) : AuthenticationException(message)

