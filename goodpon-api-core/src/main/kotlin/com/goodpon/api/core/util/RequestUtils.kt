package com.goodpon.api.core.util

import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder

object RequestUtils {

    private const val TRACE_ID_ATTRIBUTE = "traceId"
    private const val DEFAULT_TRACE_ID = "N/A"

    fun getTraceId(): String {
        val attributes = RequestContextHolder.getRequestAttributes()
            ?: return DEFAULT_TRACE_ID
        val traceId = attributes.getAttribute(TRACE_ID_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST) as String?
        if (traceId.isNullOrEmpty()) {
            return DEFAULT_TRACE_ID
        }
        return traceId
    }
}