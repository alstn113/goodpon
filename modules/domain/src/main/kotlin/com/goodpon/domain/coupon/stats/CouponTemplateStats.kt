package com.goodpon.domain.coupon.stats

data class CouponTemplateStats private constructor(
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

    companion object {
        fun create(couponTemplateId: Long): CouponTemplateStats {
            return CouponTemplateStats(
                couponTemplateId = couponTemplateId,
                issueCount = 0L,
                redeemCount = 0L
            )
        }

        fun reconstruct(
            couponTemplateId: Long,
            issueCount: Long,
            redeemCount: Long,
        ): CouponTemplateStats {
            return CouponTemplateStats(
                couponTemplateId = couponTemplateId,
                issueCount = issueCount,
                redeemCount = redeemCount
            )
        }
    }
}