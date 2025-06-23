package com.goodpon.infra.db.coupon

import com.goodpon.core.domain.coupon.CouponTemplate
import com.goodpon.core.domain.coupon.CouponTemplateFactory
import com.goodpon.core.domain.coupon.vo.CouponLimitType
import com.goodpon.core.domain.coupon.vo.CouponTemplateStatus
import com.goodpon.core.domain.coupon.vo.CouponDiscountType
import com.goodpon.infra.db.AuditableEntity
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
    val usageEndAt: LocalDateTime? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val limitType: CouponLimitType,

    @Column
    val maxIssueLimit: Long? = null,

    @Column
    val maxUsageLimit: Long? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: CouponTemplateStatus,

    @Column(nullable = false)
    val isIssuable: Boolean,

    @Column(nullable = false)
    val isUsable: Boolean,
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
            issueStartAt = issueStartAt,
            issueEndAt = issueEndAt,
            validityDays = validityDays,
            usageEndAt = usageEndAt,
            limitType = limitType,
            maxIssueLimit = maxIssueLimit,
            maxUsageLimit = maxUsageLimit,
            status = status,
            isIssuable = isIssuable,
            isUsable = isUsable
        )
    }

    fun update(couponTemplate: CouponTemplate) {}

    companion object {

        fun fromDomain(domain: CouponTemplate): CouponTemplateEntity {
            return CouponTemplateEntity(
                merchantId = domain.merchantId,
                name = domain.name,
                description = domain.description,
                minOrderAmount = domain.usageCondition.minOrderAmount,
                discountType = domain.discountPolicy.discountType,
                discountValue = domain.discountPolicy.discountValue,
                issueStartAt = domain.period.issueStartAt,
                issueEndAt = domain.period.issueEndAt,
                validityDays = domain.period.validityDays,
                usageEndAt = domain.period.usageEndAt,
                limitType = domain.limitPolicy.limitType,
                maxIssueLimit = domain.limitPolicy.maxIssueLimit,
                maxUsageLimit = domain.limitPolicy.maxUsageLimit,
                status = domain.status,
                isIssuable = domain.isIssuable,
                isUsable = domain.isUsable
            )
        }
    }
}
