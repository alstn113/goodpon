package com.goodpon.partner.openapi.advice

import com.goodpon.domain.BaseException
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuanceLimitExceededException
import com.goodpon.domain.coupon.template.exception.CouponTemplateIssuancePeriodException
import com.goodpon.domain.coupon.template.exception.CouponTemplateStatusNotIssuableException
import com.goodpon.partner.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.partner.application.coupon.service.exception.UserCouponAlreadyIssuedException
import com.goodpon.partner.openapi.response.ApiResponse
import com.goodpon.partner.openapi.response.ErrorType
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(1)
@RestControllerAdvice(basePackages = ["com.goodpon.partner.openapi"])
class PartnerOpenApiControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ApiResponse<Unit>> {
        log.warn("BaseException : {}", e.message, e)

        val errorType = when (e) {
            // issue coupon
            is CouponTemplateNotFoundException -> ErrorType.COUPON_TEMPLATE_NOT_FOUND
            is CouponTemplateNotOwnedByMerchantException -> ErrorType.COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT
            is UserCouponAlreadyIssuedException -> ErrorType.COUPON_ALREADY_ISSUED
            is CouponTemplateIssuancePeriodException -> ErrorType.COUPON_NOT_ISSUABLE_PERIOD
            is CouponTemplateStatusNotIssuableException -> ErrorType.COUPON_TEMPLATE_NOT_ISSUABLE
            is CouponTemplateIssuanceLimitExceededException -> ErrorType.COUPON_TEMPLATE_MAX_ISSUE_COUNT_EXCEEDED

            else -> ErrorType.INTERNAL_SERVER_ERROR
        }

        val response = ApiResponse.error(errorType)
        return ResponseEntity(response, errorType.status)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error("Exception : {}", e.message, e)

        val errorType = ErrorType.INTERNAL_SERVER_ERROR
        val response = ApiResponse.error(error = errorType)
        return ResponseEntity(response, errorType.status)
    }
}
