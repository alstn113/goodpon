package com.goodpon.core.domain.coupon.vo

enum class CouponLimitType {
    NONE,
    ISSUE_COUNT,
    REDEEM_COUNT,
    ;

    fun isNotIssueCountLimit(): Boolean {
        return this != ISSUE_COUNT
    }

    fun isNotRedeemCountLimit(): Boolean {
        return this != REDEEM_COUNT
    }
}