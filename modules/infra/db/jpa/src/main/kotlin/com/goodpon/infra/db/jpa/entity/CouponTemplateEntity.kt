package com.goodpon.infra.db.jpa.entity

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.CouponTemplateFactory
import com.goodpon.domain.coupon.template.vo.CouponDiscountType
import com.goodpon.domain.coupon.template.vo.CouponLimitPolicyType
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_templates")
class CouponTemplateEntity(
    @Column(nullable = false)
    val merchantId: Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val description: String,

    @Column
    val minOrderAmount: Int? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val discountType: CouponDiscountType,

    @Column(nullable = false)
    val discountValue: Int,

    @Column
    val maxDiscountAmount: Int? = null,

    @Column(nullable = false)
    val issueStartAt: LocalDateTime,

    @Column
    val issueEndAt: LocalDateTime? = null,

    @Column
    val validityDays: Int? = null,

    @Column
    val absoluteExpiresAt: LocalDateTime? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val limitType: CouponLimitPolicyType,

    @Column
    val maxIssueCount: Long? = null,

    @Column
    val maxRedeemCount: Long? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: CouponTemplateStatus,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun update(couponTemplate: CouponTemplate) {
        this.status = couponTemplate.status
    }

    fun toDomain(): CouponTemplate {
        return CouponTemplateFactory.reconstruct(
            id = id,
            merchantId = merchantId,
            name = name,
            description = description,
            minOrderAmount = minOrderAmount,
            discountType = discountType,
            discountValue = discountValue,
            maxDiscountAmount = maxDiscountAmount,
            issueStartAt = issueStartAt,
            issueEndAt = issueEndAt,
            validityDays = validityDays,
            absoluteExpiresAt = absoluteExpiresAt,
            limitType = limitType,
            maxIssueCount = maxIssueCount,
            maxRedeemCount = maxRedeemCount,
            status = status,
        )
    }

    companion object {
        fun fromDomain(domain: CouponTemplate): CouponTemplateEntity {
            return CouponTemplateEntity(
                merchantId = domain.merchantId,
                name = domain.name,
                description = domain.description,
                minOrderAmount = domain.redemptionCondition.minOrderAmount,
                discountType = domain.discountPolicy.discountType,
                discountValue = domain.discountPolicy.discountValue,
                maxDiscountAmount = domain.discountPolicy.maxDiscountAmount,
                issueStartAt = domain.period.issueStartAt,
                issueEndAt = domain.period.issueEndAt,
                validityDays = domain.period.validityDays,
                absoluteExpiresAt = domain.period.absoluteExpiresAt,
                limitType = domain.limitPolicy.limitType,
                maxIssueCount = domain.limitPolicy.maxIssueCount,
                maxRedeemCount = domain.limitPolicy.maxRedeemCount,
                status = domain.status,
            )
        }
    }
}
