package com.goodpon.api.partner.advice

import com.goodpon.api.partner.idempotency.exception.IdempotencyRequestPayloadMismatchException
import com.goodpon.api.partner.idempotency.exception.IdempotencyRequestProcessingException
import com.goodpon.api.partner.idempotency.exception.InvalidIdempotencyKeyException
import com.goodpon.api.partner.response.ApiResponse
import com.goodpon.api.partner.response.ErrorType
import com.goodpon.application.partner.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.application.partner.coupon.service.exception.*
import com.goodpon.domain.BaseException
import com.goodpon.domain.coupon.template.exception.*
import com.goodpon.domain.coupon.user.exception.UserCouponAlreadyRedeemedException
import com.goodpon.domain.coupon.user.exception.UserCouponExpiredException
import com.goodpon.domain.coupon.user.exception.UserCouponRedeemNotAllowedException
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Order(1)
@RestControllerAdvice(basePackages = ["com.goodpon.api.partner"])
class PartnerOpenApiControllerAdvice {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException): ResponseEntity<ApiResponse<Unit>> {
        log.warn("BaseException : {}", e.message, e)

        val errorType = when (e) {
            // idempotency
            is InvalidIdempotencyKeyException -> ErrorType.INVALID_IDEMPOTENCY_KEY
            is IdempotencyRequestProcessingException -> ErrorType.IDEMPOTENT_REQUEST_PROCESSING
            is IdempotencyRequestPayloadMismatchException -> ErrorType.IDEMPOTENT_REQUEST_PAYLOAD_MISMATCH

            // coupon template
            is CouponTemplateIssuanceLimitExceededException -> ErrorType.COUPON_TEMPLATE_MAX_ISSUE_COUNT_EXCEEDED
            is CouponTemplateIssuancePeriodException -> ErrorType.COUPON_NOT_ISSUABLE_PERIOD
            is CouponTemplateNotFoundException -> ErrorType.COUPON_TEMPLATE_NOT_FOUND
            is CouponTemplateNotOwnedByMerchantException -> ErrorType.COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT
            is CouponTemplateStatusNotIssuableException -> ErrorType.COUPON_TEMPLATE_NOT_ISSUABLE

            // user coupon
            is UserCouponAlreadyIssuedException -> ErrorType.COUPON_ALREADY_ISSUED
            is UserCouponNotFoundException -> ErrorType.USER_COUPON_NOT_FOUND
            is UserCouponNotOwnedByUserException -> ErrorType.USER_COUPON_NOT_OWNED_BY_USER

            // redeem
            is CouponTemplateRedemptionConditionNotSatisfiedException -> ErrorType.COUPON_TEMPLATE_REDEEM_CONDITION_NOT_MET
            is CouponTemplateRedemptionLimitExceededException -> ErrorType.COUPON_TEMPLATE_MAX_REDEEM_COUNT_EXCEEDED
            is CouponTemplateStatusNotRedeemableException,
            is UserCouponRedeemNotAllowedException,
                -> ErrorType.COUPON_NOT_REDEEMABLE

            is UserCouponAlreadyRedeemedException -> ErrorType.USER_COUPON_ALREADY_REDEEMED
            is UserCouponExpiredException -> ErrorType.USER_COUPON_EXPIRED

            // cancel redemption
            is UserCouponAlreadyCanceledException -> ErrorType.USER_COUPON_ALREADY_CANCELLED
            is UserCouponCancelRedemptionNotAllowedException -> ErrorType.USER_COUPON_CANCEL_NOT_ALLOWED
            is CouponOrderIdMismatchException -> ErrorType.USER_COUPON_CANCEL_ORDER_ID_MISMATCH

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
