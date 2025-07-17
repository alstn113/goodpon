package com.goodpon.dashboard.api.advice

import com.goodpon.dashboard.api.response.ApiErrorDetail
import com.goodpon.dashboard.api.response.ApiResponse
import com.goodpon.dashboard.api.response.ErrorType
import com.goodpon.dashboard.application.account.port.out.exception.AccountNotFoundException
import com.goodpon.dashboard.application.account.service.exception.AccountEmailExistsException
import com.goodpon.dashboard.application.auth.service.exception.EmailVerificationNotFoundException
import com.goodpon.dashboard.application.auth.service.exception.PasswordMismatchException
import com.goodpon.dashboard.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.dashboard.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.BaseException
import com.goodpon.domain.account.exception.*
import com.goodpon.domain.coupon.template.exception.CouponTemplateInvalidStatusToPublishException
import com.goodpon.domain.coupon.template.exception.creation.CouponTemplateValidationException
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
            is AccountEmailExistsException -> ErrorType.ACCOUNT_EMAIL_ALREADY_EXISTS

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
            is NoMerchantAccessPermissionException -> ErrorType.NO_MERCHANT_ACCESS_PERMISSION

            // Coupon Template
            is CouponTemplateNotFoundException -> ErrorType.COUPON_TEMPLATE_NOT_FOUND
            is CouponTemplateInvalidStatusToPublishException -> ErrorType.COUPON_TEMPLATE_INVALID_STATUS_TO_PUBLISH
            is CouponTemplateNotOwnedByMerchantException -> ErrorType.COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT

            // Common
            else -> ErrorType.INTERNAL_SERVER_ERROR
        }

        log.warn("BaseException : {}", e.message, e)

        val response = ApiResponse.error(errorType)
        return ResponseEntity(response, errorType.status)
    }

    @ExceptionHandler(CouponTemplateValidationException::class)
    fun handleCouponTemplateCreationException(e: CouponTemplateValidationException): ResponseEntity<ApiResponse<Unit>> {
        log.warn("CouponTemplateCreationException : {}, errors: {}", e.message, e.errors, e)

        val errorDetails = e.errors.map { error ->
            ApiErrorDetail(field = error.field, message = error.message)
        }

        val response = ApiResponse.error(ErrorType.COUPON_TEMPLATE_VALIDATION_FAILED, errorDetails)
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error("Exception : {}", e.message, e)

        val errorType = ErrorType.INTERNAL_SERVER_ERROR
        val response = ApiResponse.error(error = errorType)
        return ResponseEntity(response, errorType.status)
    }
}