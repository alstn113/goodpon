package com.goodpon.infra.redis.coupon.core

enum class CouponRedeemResult {

    SUCCESS,
    ALREADY_REDEEMED,
    REDEEM_LIMIT_EXCEEDED,
}