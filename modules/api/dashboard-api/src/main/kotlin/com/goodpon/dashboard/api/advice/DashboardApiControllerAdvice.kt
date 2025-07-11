package com.goodpon.dashboard.api.advice

import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.response.ErrorType
import com.goodpon.dashboard.application.account.port.out.exception.AccountNotFoundException
import com.goodpon.dashboard.application.account.service.exception.AccountEmailExistsException
import com.goodpon.dashboard.application.auth.service.exception.PasswordMismatchException
import com.goodpon.domain.BaseException
import com.goodpon.domain.account.exception.*
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(basePackages = ["com.goodpon.dashboard.api"])
class DashboardApiControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ApiResponse<Unit>> {
        val errorType = when (e) {
            is AccountEmailExistsException,
                -> ErrorType.ACCOUNT_EMAIL_ALREADY_EXISTS

            is AccountInvalidEmailFormatException,
            is AccountInvalidNameLengthException,
            is AccountInvalidPasswordLengthException,
            is AccountNameBlankException,
                -> ErrorType.ACCOUNT_SIGN_UP_INVALID_INPUT

            is PasswordMismatchException,
                -> ErrorType.PASSWORD_MISMATCH

            is AccountAlreadyVerifiedException,
                -> ErrorType.ACCOUNT_ALREADY_VERIFIED

            is AccountNotFoundException,
                -> ErrorType.ACCOUNT_NOT_FOUND

            else -> ErrorType.INTERNAL_SERVER_ERROR
        }

        log.error("BaseException : {}", e.message, e)

        val response = ApiResponse.error<Unit>(errorType)
        return ResponseEntity(response, errorType.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error("Exception : {}", e.message, e)

        val response = ApiResponse.error<Unit>(ErrorType.INTERNAL_SERVER_ERROR)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}