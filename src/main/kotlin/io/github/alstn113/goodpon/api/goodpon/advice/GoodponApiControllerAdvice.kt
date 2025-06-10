package io.github.alstn113.goodpon.api.goodpon.advice

import io.github.alstn113.goodpon.api.goodpon.response.ApiErrorResponse
import io.github.alstn113.goodpon.support.error.CoreException
import io.github.alstn113.goodpon.support.error.ErrorLevel
import io.github.alstn113.goodpon.support.error.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder

@RestControllerAdvice(basePackages = ["io.github.alstn113.goodpon.api.goodpon"])
class GoodponApiControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiErrorResponse> {
        when (e.errorType.errorLevel) {
            ErrorLevel.ERROR -> log.error("CoreException : {}", e.message, e)
            ErrorLevel.WARN -> log.warn("CoreException : {}", e.message, e)
            else -> log.info("CoreException : {}", e.message, e)
        }

        val traceId = getTraceId()
        val response = ApiErrorResponse.of(traceId, e.errorType)
        val status = HttpStatus.valueOf(e.errorType.statusCode)
        return ResponseEntity(response, status)
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
