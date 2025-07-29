package com.goodpon.api.partner.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.api.partner.response.ApiResponse
import com.goodpon.api.partner.response.ErrorType
import com.goodpon.api.partner.response.TraceIdProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class ApiKeyAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
    private val traceIdProvider: TraceIdProvider,
) : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        log.warn("Authentication failed: {}", authException.message)

        val (status, errorType) = when (authException) {
            is ClientIdMissingException -> HttpServletResponse.SC_BAD_REQUEST to ErrorType.CLIENT_ID_MISSING
            is ClientSecretMissingException -> HttpServletResponse.SC_BAD_REQUEST to ErrorType.CLIENT_SECRET_MISSING
            is InvalidCredentialsException -> HttpServletResponse.SC_UNAUTHORIZED to ErrorType.INVALID_CREDENTIALS
            else -> HttpServletResponse.SC_UNAUTHORIZED to ErrorType.UNAUTHORIZED
        }

        response.status = status
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"

        val errorResponse = ApiResponse.error(
            error = errorType,
            traceId = traceIdProvider.getTraceId()
        )
        val body = objectMapper.writeValueAsString(errorResponse)

        response.writer.write(body)
    }
}