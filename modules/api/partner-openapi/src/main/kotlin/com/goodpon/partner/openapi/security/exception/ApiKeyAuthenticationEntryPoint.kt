package com.goodpon.partner.openapi.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.partner.openapi.response.ApiErrorResponse
import com.goodpon.partner.openapi.response.TraceIdProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class ApiKeyAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper,
    private val traceIdProvider: TraceIdProvider,
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED

        val traceId = traceIdProvider.getTraceId()
        val errorResponse = ApiErrorResponse.of(traceId, "UNAUTHORIZED")
        val body = objectMapper.writeValueAsString(errorResponse)

        response.writer.write(body)
    }
}