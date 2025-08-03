package com.goodpon.api.partner.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

class TraceIdInterceptor(
    private val traceIdProvider: TraceIdProvider,
) : HandlerInterceptor {

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        val traceId = traceIdProvider.getTraceId()
        response.setHeader("X-Goodpon-Trace-Id", traceId)
    }
}