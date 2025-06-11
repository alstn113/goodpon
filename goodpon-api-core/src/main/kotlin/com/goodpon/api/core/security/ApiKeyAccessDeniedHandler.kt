package com.goodpon.api.core.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.api.core.api.response.ApiErrorResponse
import com.goodpon.api.core.util.RequestUtils
import com.goodpon.common.support.error.ErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class ApiKeyAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_FORBIDDEN

        val traceId = RequestUtils.getTraceId()
        val errorResponse = ApiErrorResponse.of(traceId, ErrorType.FORBIDDEN_REQUEST)
        val body = objectMapper.writeValueAsString(errorResponse)

        response.writer.write(body)
    }
}
