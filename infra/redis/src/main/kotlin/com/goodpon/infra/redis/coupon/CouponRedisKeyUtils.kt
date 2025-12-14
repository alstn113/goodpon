package com.goodpon.infra.redis.coupon

object CouponRedisKeyUtils {

    private const val RESERVED_ZSET_KEY_PREFIX = "coupon:reserved:"
    private const val ISSUED_SET_KEY_PREFIX = "coupon:issued:"
    private const val REDEEMED_SET_KEY_PREFIX = "coupon:redeemed:"

    fun reservedKey(couponTemplateId: Long) = "$RESERVED_ZSET_KEY_PREFIX$couponTemplateId"
    fun issuedKey(couponTemplateId: Long) = "$ISSUED_SET_KEY_PREFIX$couponTemplateId"
    fun redeemedKey(couponTemplateId: Long) = "$REDEEMED_SET_KEY_PREFIX$couponTemplateId"

    fun reservedKeyPattern() = "$RESERVED_ZSET_KEY_PREFIX*"
    fun issuedKeyPattern() = "$ISSUED_SET_KEY_PREFIX*"
    fun redeemedKeyPattern() = "$REDEEMED_SET_KEY_PREFIX*"

    fun extractCouponTemplateId(key: String) = key.substringAfterLast(":").toLong()
}