package com.goodpon.core.domain.coupon.vo

enum class CouponTemplateStatus {

    DRAFT,
    ISSUABLE,
    EXPIRED,
    DISCARDED,
    ;

    fun isNotIssuable(): Boolean {
        return this != ISSUABLE
    }

    fun isNotRedeemable(): Boolean {
        return this == DRAFT || this == DISCARDED
    }
}