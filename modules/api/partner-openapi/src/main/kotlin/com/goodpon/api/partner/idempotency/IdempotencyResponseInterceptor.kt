package com.goodpon.api.partner.idempotency

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodpon.application.partner.idempotency.port.`in`.IdempotencyUseCase
import com.goodpon.application.partner.idempotency.service.IdempotencyResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingResponseWrapper

class IdempotencyResponseInterceptor(
    private val idempotencyUseCase: IdempotencyUseCase,
    private val objectMapper: ObjectMapper,
) : HandlerInterceptor {

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        if (!hasIdempotentAnnotation(handler)) {
            return
        }

        val idempotencyKey = IdempotencyUtils.getIdempotencyKey(request)
        if (idempotencyKey.isNullOrBlank()) {
            return
        }

        val key = IdempotencyUtils.generateKey(request, idempotencyKey)

        val responseWrapper = response as ContentCachingResponseWrapper
        val bodyBytes = responseWrapper.contentAsByteArray
        val body = objectMapper.readTree(bodyBytes)
        val headers = responseWrapper.headerNames.associateWith { name ->
            responseWrapper.getHeaders(name).toList()
        }

        val idempotencyResponse = IdempotencyResponse(
            status = response.status,
            headers = headers,
            body = body
        )
        idempotencyUseCase.saveCompleted(key, idempotencyResponse)
    }

    private fun hasIdempotentAnnotation(handler: Any): Boolean {
        if (handler is HandlerMethod) {
            return handler.getMethodAnnotation(Idempotent::class.java) != null
        }
        return false
    }
}