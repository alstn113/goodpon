package com.goodpon.core.domain.coupon.vo

enum class CouponLimitType {

    NONE,
    ISSUE_COUNT,
    USAGE_COUNT,
    ;

    fun isNotIssueCountLimit(): Boolean {
        return this != ISSUE_COUNT
    }

    fun isNotUsageCountLimit(): Boolean {
        return this != USAGE_COUNT
    }
}