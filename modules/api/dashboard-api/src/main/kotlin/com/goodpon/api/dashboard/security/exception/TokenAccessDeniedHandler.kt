package com.goodpon.api.dashboard.security.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.api.dashboard.response.ApiResponse
import com.goodpon.api.dashboard.response.ErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class TokenAccessDeniedHandler(
    private val objectMapper: ObjectMapper,
) : AccessDeniedHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException,
    ) {
        val errorType = when (accessDeniedException) {
            is AccountNotVerifiedException -> ErrorType.ACCOUNT_NOT_VERIFIED
            else -> ErrorType.FORBIDDEN
        }

        log.warn("Access denied: {}", accessDeniedException.message)

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_FORBIDDEN

        val errorResponse = ApiResponse.error(errorType)
        val body = objectMapper.writeValueAsString(errorResponse)

        response.writer.write(body)
    }
}