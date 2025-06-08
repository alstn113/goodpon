package io.github.alstn113.goodpon.api.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.MDC
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class MdcLoggingFilter : Filter {
    companion object {
        private const val TRACE_ID = "traceId"
    }

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        filterChain: FilterChain,
    ) {
        val httpRequest = request as HttpServletRequest
        val traceId = httpRequest.getHeader("X-Trace-Id") ?: UUID.randomUUID().toString()

        try {
            MDC.put(TRACE_ID, traceId)
            httpRequest.setAttribute(TRACE_ID, traceId)
            filterChain.doFilter(request, response)
        } finally {
            MDC.remove(TRACE_ID)
        }
    }
}
