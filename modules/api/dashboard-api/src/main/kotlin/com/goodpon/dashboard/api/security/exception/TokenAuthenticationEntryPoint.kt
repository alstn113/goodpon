package com.goodpon.dashboard.api.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.response.ErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class TokenAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
) : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        log.warn("Authentication failed: {}", authException.message)

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val errorResponse = ApiResponse.error<Unit>(ErrorType.UNAUTHORIZED)
        val body = objectMapper.writeValueAsString(errorResponse)

        response.writer.write(body)
    }
}