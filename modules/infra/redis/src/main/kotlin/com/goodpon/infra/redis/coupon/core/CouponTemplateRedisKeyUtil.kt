package com.goodpon.infra.redis.coupon.core

object CouponTemplateRedisKeyUtil {

    const val COUPON_TEMPLATE_STATS_ISSUE_SET_KEY_PREFIX = "coupon-template-stats:issue:"
    const val COUPON_TEMPLATE_STATS_REDEEM_SET_KEY_PREFIX = "coupon-template-stats:redeem:"

    fun buildIssueSetKey(couponTemplateId: Long): String {
        return "$COUPON_TEMPLATE_STATS_ISSUE_SET_KEY_PREFIX$couponTemplateId"
    }

    fun buildRedeemSetKey(couponTemplateId: Long): String {
        return "$COUPON_TEMPLATE_STATS_REDEEM_SET_KEY_PREFIX$couponTemplateId"
    }
}