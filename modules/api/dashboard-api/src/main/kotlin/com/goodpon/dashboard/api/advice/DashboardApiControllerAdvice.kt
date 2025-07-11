package com.goodpon.dashboard.api.advice

import com.goodpon.dashboard.api.response.ApiErrorDetail
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.response.ErrorType
import com.goodpon.dashboard.application.account.port.out.exception.AccountNotFoundException
import com.goodpon.dashboard.application.account.service.exception.AccountEmailExistsException
import com.goodpon.dashboard.application.auth.service.exception.EmailVerificationNotFoundException
import com.goodpon.dashboard.application.auth.service.exception.PasswordMismatchException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.BaseException
import com.goodpon.domain.account.exception.*
import com.goodpon.domain.coupon.template.exception.creation.CouponTemplateCreationException
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
            // Auth, Account
            is AccountEmailExistsException,
                -> ErrorType.ACCOUNT_EMAIL_ALREADY_EXISTS

            is AccountInvalidEmailFormatException,
            is AccountInvalidNameLengthException,
            is AccountInvalidPasswordLengthException,
            is AccountNameBlankException,
                -> ErrorType.ACCOUNT_SIGN_UP_INVALID_INPUT

            is PasswordMismatchException -> ErrorType.PASSWORD_MISMATCH
            is AccountAlreadyVerifiedException -> ErrorType.ACCOUNT_ALREADY_VERIFIED
            is AccountNotFoundException -> ErrorType.ACCOUNT_NOT_FOUND
            is EmailVerificationNotFoundException -> ErrorType.INVALID_EMAIL_VERIFICATION_TOKEN

            // Merchant
            is MerchantNotFoundException -> ErrorType.MERCHANT_NOT_FOUND

            // Common
            else -> ErrorType.INTERNAL_SERVER_ERROR
        }

        log.warn("BaseException : {}", e.message, e)

        val response = ApiResponse.error<Unit>(errorType)
        return ResponseEntity(response, errorType.status)
    }

    @ExceptionHandler(CouponTemplateCreationException::class)
    fun handleCouponTemplateCreationException(e: CouponTemplateCreationException): ResponseEntity<ApiResponse<Unit>> {
        log.warn("CouponTemplateCreationException : {}, errors: {}", e.message, e.errors, e)

        val errorDetails = e.errors.map { error ->
            ApiErrorDetail(field = error.field, message = error.message)
        }

        val response = ApiResponse.error<Unit>(ErrorType.COUPON_TEMPLATE_CREATION_FAILED, errorDetails)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }


    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error("Exception : {}", e.message, e)

        val response = ApiResponse.error<Unit>(ErrorType.INTERNAL_SERVER_ERROR)
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}