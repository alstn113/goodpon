package com.goodpon.core.domain.coupon.template.vo

enum class CouponLimitPolicyType {
    NONE,
    ISSUE_COUNT,
    REDEEM_COUNT,
    ;

    fun isNotIssueCountLimit(): Boolean = this != ISSUE_COUNT
    fun isNotRedeemCountLimit(): Boolean = this != REDEEM_COUNT
}