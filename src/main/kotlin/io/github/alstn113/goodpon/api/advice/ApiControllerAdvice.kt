package io.github.alstn113.goodpon.api.advice

import io.github.alstn113.goodpon.support.error.CoreException
import io.github.alstn113.goodpon.support.error.ErrorType
import io.github.alstn113.goodpon.support.response.ApiErrorResponse
import io.netty.handler.logging.LogLevel
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder

@RestControllerAdvice
class ApiControllerAdvice {
    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiErrorResponse> {
        when (e.errorType.logLevel) {
            LogLevel.ERROR -> log.error("CoreException : {}", e.message, e)
            LogLevel.WARN -> log.warn("CoreException : {}", e.message, e)
            else -> log.info("CoreException : {}", e.message, e)
        }

        val traceId = getTraceId()
        val response = ApiErrorResponse.of(traceId, e.errorType)
        return ResponseEntity(response, e.errorType.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiErrorResponse> {
        log.error("Exception : {}", e.message, e)

        val traceId = getTraceId()
        val response = ApiErrorResponse.of(traceId, ErrorType.COMMON_ERROR)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun getTraceId(): String {
        val reqAttributes = RequestContextHolder.getRequestAttributes()
        return reqAttributes?.getAttribute("traceId", RequestAttributes.SCOPE_REQUEST) as? String ?: "N/A"
    }
}
