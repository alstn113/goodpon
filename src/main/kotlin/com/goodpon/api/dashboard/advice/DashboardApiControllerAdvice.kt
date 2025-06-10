package com.goodpon.api.dashboard.advice

import com.goodpon.goodpon.api.dashboard.response.ApiResponse
import com.goodpon.goodpon.support.error.CoreException
import com.goodpon.goodpon.support.error.ErrorLevel
import com.goodpon.goodpon.support.error.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.goodpon.goodpon.api.dashboard"])
class DashboardApiControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(CoreException::class)
    fun handleCoreException(e: CoreException): ResponseEntity<ApiResponse<Any>> {
        when (e.errorType.errorLevel) {
            ErrorLevel.ERROR -> log.error("CoreException : {}", e.message, e)
            ErrorLevel.WARN -> log.warn("CoreException : {}", e.message, e)
            else -> log.info("CoreException : {}", e.message, e)
        }

        val response = ApiResponse.error<Any>(e.errorType, e.data)
        val status = HttpStatus.valueOf(e.errorType.statusCode)
        return ResponseEntity(response, status)

    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)

        val response = ApiResponse.error<Any>(ErrorType.COMMON_ERROR, null)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}