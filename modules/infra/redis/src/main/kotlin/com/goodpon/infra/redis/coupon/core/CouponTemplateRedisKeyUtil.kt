package com.goodpon.infra.redis.coupon.core

object CouponTemplateRedisKeyUtil {

    const val COUPON_RESERVED_SET_KEY_PREFIX = "coupon:reserved:"
    const val COUPON_ISSUED_SET_KEY_PREFIX = "coupon:issued:"
    const val COUPON_REDEEMED_SET_KEY_PREFIX = "coupon:redeemed:"

    fun buildReservedSetKey(couponTemplateId: Long): String {
        return "$COUPON_RESERVED_SET_KEY_PREFIX$couponTemplateId"
    }

    fun buildIssuedSetKey(couponTemplateId: Long): String {
        return "$COUPON_ISSUED_SET_KEY_PREFIX$couponTemplateId"
    }

    fun buildRedeemedSetKey(couponTemplateId: Long): String {
        return "$COUPON_REDEEMED_SET_KEY_PREFIX$couponTemplateId"
    }
}