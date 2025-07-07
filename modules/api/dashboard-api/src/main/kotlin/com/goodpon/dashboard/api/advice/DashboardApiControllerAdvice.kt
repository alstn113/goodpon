package com.goodpon.dashboard.api.advice

import com.goodpon.dashboard.api.response.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.goodpon.api.dashboard"])
class DashboardApiControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Any>> {
        log.error("Exception : {}", e.message, e)

        val response = ApiResponse.error<Any>("COMMON_ERROR", null)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}