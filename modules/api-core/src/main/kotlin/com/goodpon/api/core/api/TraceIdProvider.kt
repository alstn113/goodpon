package com.goodpon.api.core.api

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