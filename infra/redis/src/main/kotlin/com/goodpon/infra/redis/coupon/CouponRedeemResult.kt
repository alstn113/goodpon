package com.goodpon.infra.redis.coupon

enum class CouponRedeemResult {

    SUCCESS,
    ALREADY_REDEEMED,
    REDEEM_LIMIT_EXCEEDED,
}