package io.github.alstn113.goodpon.api.dashboard.advice

import io.github.alstn113.goodpon.api.dashboard.response.ApiResponse
import io.github.alstn113.goodpon.support.error.CoreException
import io.github.alstn113.goodpon.support.error.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.boot.logging.LogLevel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["io.github.alstn113.goodpon.api.dashboard"])
class DashboardApiControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiResponse<Any>> {
        when (e.errorType.logLevel) {
            LogLevel.ERROR -> log.error("CoreException : {}", e.message, e)
            LogLevel.WARN -> log.warn("CoreException : {}", e.message, e)
            else -> log.info("CoreException : {}", e.message, e)
        }

        val response = ApiResponse.error<Any>(e.errorType, e.data)
        return ResponseEntity(response, e.errorType.status)

    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)

        val response = ApiResponse.error<Any>(ErrorType.COMMON_ERROR, null)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}