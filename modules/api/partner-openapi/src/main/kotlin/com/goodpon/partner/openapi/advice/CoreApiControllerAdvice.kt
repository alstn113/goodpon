package com.goodpon.partner.openapi.advice

import com.goodpon.domain.support.error.CoreException
import com.goodpon.domain.support.error.ErrorLevel
import com.goodpon.domain.support.error.ErrorType
import com.goodpon.partner.openapi.response.ApiErrorResponse
import com.goodpon.partner.openapi.response.TraceIdProvider
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.goodpon.goodpon.api.goodpon"])
class CoreApiControllerAdvice(
    private val traceIdProvider: TraceIdProvider,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiErrorResponse> {
        when (e.errorType.errorLevel) {
            ErrorLevel.ERROR -> log.error("CoreException : {}", e.message, e)
            ErrorLevel.WARN -> log.warn("CoreException : {}", e.message, e)
            else -> log.info("CoreException : {}", e.message, e)
        }

        val traceId = traceIdProvider.getTraceId()
        val response = ApiErrorResponse.of(traceId, e.errorType)
        val status = HttpStatus.valueOf(e.errorType.statusCode)
        return ResponseEntity(response, status)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiErrorResponse> {
        log.error("Exception : {}", e.message, e)

        val traceId = traceIdProvider.getTraceId()
        val response = ApiErrorResponse.of(traceId, ErrorType.COMMON_ERROR)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
