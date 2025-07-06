package com.goodpon.partner.openapi.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.domain.support.error.ErrorType
import com.goodpon.partner.openapi.response.ApiErrorResponse
import com.goodpon.partner.openapi.response.TraceIdProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class ApiKeyAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
    private val tracerIdProvider: TraceIdProvider,
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_FORBIDDEN

        val traceId = tracerIdProvider.getTraceId()
        val errorResponse = ApiErrorResponse.of(traceId, ErrorType.FORBIDDEN_REQUEST)
        val body = objectMapper.writeValueAsString(errorResponse)

        response.writer.write(body)
    }
}
