package com.goodpon.infra.jpa.coupon

import com.goodpon.domain.coupon.stats.CouponTemplateStats
import com.goodpon.infra.jpa.AuditableEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class CouponTemplateStatsEntity(
    @Id
    @Column(name = "coupon_template_id")
    val couponTemplateId: Long,

    @Column(name = "issue_count", nullable = false)
    var issueCount: Long,

    @Column(name = "redeem_count", nullable = false)
    var redeemCount: Long,
) : AuditableEntity() {

    fun update(couponTemplateStats: CouponTemplateStats) {
        this.issueCount = couponTemplateStats.issueCount
        this.redeemCount = couponTemplateStats.redeemCount
    }

    fun toDomain(): CouponTemplateStats {
        return CouponTemplateStats(
            couponTemplateId = this.couponTemplateId,
            issueCount = this.issueCount,
            redeemCount = this.redeemCount
        )
    }

    companion object {
        fun fromDomain(couponTemplateStats: CouponTemplateStats): CouponTemplateStatsEntity {
            return CouponTemplateStatsEntity(
                couponTemplateId = couponTemplateStats.couponTemplateId,
                issueCount = couponTemplateStats.issueCount,
                redeemCount = couponTemplateStats.redeemCount
            )
        }
    }
}