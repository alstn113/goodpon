package com.goodpon.core.domain.coupon

data class CouponTemplateStats(
    val couponTemplateId: Long,
    val issueCount: Long,
    val redeemCount: Long,
) {
    fun incrementIssueCount(): CouponTemplateStats {
        return this.copy(issueCount = issueCount + 1)
    }

    fun incrementRedeemCount(): CouponTemplateStats {
        return this.copy(redeemCount = redeemCount + 1)
    }

    fun decrementRedeemCount(): CouponTemplateStats {
        return this.copy(redeemCount = redeemCount - 1)
    }
}