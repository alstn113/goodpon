package com.goodpon.domain.coupon.template.vo

enum class CouponTemplateStatus {

    DRAFT,
    ISSUABLE,
    EXPIRED,
    DISCARDED,
    ;

    fun isNotDraft(): Boolean {
        return this != DRAFT
    }

    fun isNotIssuable(): Boolean {
        return this != ISSUABLE
    }

    fun isNotRedeemable(): Boolean {
        return this == DRAFT || this == DISCARDED
    }
}