package com.goodpon.partner.application.coupon.port.`in`.dto

enum class CouponIssuanceStatus {

    AVAILABLE,
    PERIOD_NOT_STARTED,
    PERIOD_ENDED,
    MAX_REDEEM_COUNT_EXCEEDED,
    MAX_ISSUE_COUNT_EXCEEDED,
    ALREADY_ISSUED_BY_USER,
}