package com.goodpon.api.partner.security.exception

import org.springframework.security.core.AuthenticationException

class InvalidCredentialsException(
    message: String,
) : AuthenticationException(message)

