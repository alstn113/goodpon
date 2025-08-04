package com.goodpon.api.partner.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class TraceIdFilter(
    private val traceIdProvider: TraceIdProvider,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val traceId = traceIdProvider.getTraceId()
        response.setHeader(GOODPON_TRACE_ID_HEADER, traceId)

        filterChain.doFilter(request, response)
    }

    companion object {
        private const val GOODPON_TRACE_ID_HEADER = "X-Goodpon-Trace-Id"
    }
}