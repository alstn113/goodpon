package com.goodpon.partner.openapi.response

import io.micrometer.tracing.Tracer
import org.springframework.stereotype.Component

@Component
class TraceIdProvider(
    private val tracer: Tracer,
) {

    fun getTraceId(): String {
        return tracer.currentSpan()?.context()?.traceId() ?: "N/A"
    }
}