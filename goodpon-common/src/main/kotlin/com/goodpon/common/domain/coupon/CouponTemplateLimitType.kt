package com.goodpon.common.domain.coupon

enum class CouponTemplateLimitType {

    NONE,
    ISSUE_COUNT,
    USE_COUNT,
    ;

    fun isNotIssueCount(): Boolean {
        return this != ISSUE_COUNT
    }

    fun isNotUseCount(): Boolean {
        return this != USE_COUNT
    }
}