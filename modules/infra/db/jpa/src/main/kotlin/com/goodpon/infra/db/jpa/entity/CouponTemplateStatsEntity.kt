package com.goodpon.infra.db.jpa.entity

import com.goodpon.domain.coupon.stats.CouponTemplateStats
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "coupon_template_stats")
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
        return CouponTemplateStats.reconstruct(
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