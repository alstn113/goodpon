package com.goodpon.core.domain.coupon

enum class CouponTemplateStatus {

    DRAFT,
    ISSUABLE,
    EXPIRED,
    TERMINATED,
    ;

    fun isNotIssuable(): Boolean {
        return this != ISSUABLE
    }

    fun isNotUsable(): Boolean {
        return this == DRAFT || this == TERMINATED
    }
}