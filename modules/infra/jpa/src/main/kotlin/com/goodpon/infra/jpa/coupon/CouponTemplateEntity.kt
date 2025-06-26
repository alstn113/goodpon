package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.CouponTemplate
import com.goodpon.core.domain.coupon.CouponTemplateFactory
import com.goodpon.core.domain.coupon.vo.CouponDiscountType
import com.goodpon.core.domain.coupon.vo.CouponLimitType
import com.goodpon.core.domain.coupon.vo.CouponTemplateStatus
import com.goodpon.infra.jpa.AuditableEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class CouponTemplateEntity(
    @Column(nullable = false)
    val merchantId: Long,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val description: String,

    @Column
    val minOrderAmount: Long? = null,

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
    val limitType: CouponLimitType,

    @Column
    val maxIssueLimit: Long? = null,

    @Column
    val maxRedeemLimit: Long? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: CouponTemplateStatus,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun toDomain(): CouponTemplate {
        return CouponTemplateFactory.reconstitute(
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
            maxIssueLimit = maxIssueLimit,
            maxRedeemLimit = maxRedeemLimit,
            status = status,
        )
    }

    fun update(couponTemplate: CouponTemplate) {
        this.status = couponTemplate.status
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
                maxIssueLimit = domain.limitPolicy.maxIssueLimit,
                maxRedeemLimit = domain.limitPolicy.maxRedeemLimit,
                status = domain.status,
            )
        }
    }
}
