package com.goodpon.infra.redis.coupon.core

enum class CouponIssueResult {

    SUCCESS,
    ALREADY_ISSUED,
    ISSUE_LIMIT_EXCEEDED,
}