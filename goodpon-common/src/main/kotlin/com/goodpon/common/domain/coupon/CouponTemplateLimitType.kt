package com.goodpon.common.domain.coupon

enum class CouponTemplateLimitType {

    NONE,
    ISSUE_COUNT,
    USE_COUNT,
    ;

    fun isNotIssueCountLimit(): Boolean {
        return this != ISSUE_COUNT
    }

    fun isNotUseCountLimit(): Boolean {
        return this != USE_COUNT
    }
}