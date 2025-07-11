package com.goodpon.partner.openapi.advice

import com.goodpon.partner.openapi.response.ApiErrorResponse
import com.goodpon.partner.openapi.response.TraceIdProvider
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.goodpon.partner.openapi"])
class PartnerOpenApiControllerAdvice(
    private val traceIdProvider: TraceIdProvider,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiErrorResponse> {
        log.warn("Exception : {}", e.message, e)

        val traceId = traceIdProvider.getTraceId()
        val response = ApiErrorResponse.of(traceId, "COMMON_ERROR")
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
