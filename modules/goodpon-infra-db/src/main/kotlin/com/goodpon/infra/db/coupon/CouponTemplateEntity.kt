package com.goodpon.infra.db.coupon

import com.goodpon.core.domain.coupon.CouponTemplate
import com.goodpon.core.domain.coupon.CouponTemplateFactory
import com.goodpon.core.domain.coupon.vo.CouponTemplateLimitType
import com.goodpon.core.domain.coupon.vo.CouponTemplateStatus
import com.goodpon.core.domain.coupon.vo.DiscountType
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
    val minimumOrderAmount: Long? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val discountType: DiscountType,

    @Column(nullable = false)
    val discountAmount: Int,

    @Column(nullable = false)
    val issueStartAt: LocalDateTime,

    @Column
    val issueEndAt: LocalDateTime? = null,

    @Column
    val validityDays: Int? = null,

    @Column
    val useEndAt: LocalDateTime? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val limitType: CouponTemplateLimitType,

    @Column
    val issueLimit: Long? = null,

    @Column
    val useLimit: Long? = null,

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
            minimumOrderAmount = minimumOrderAmount,
            discountType = discountType,
            discountValue = discountAmount,
            issueStartAt = issueStartAt,
            issueEndAt = issueEndAt,
            validityDays = validityDays,
            useEndAt = useEndAt,
            limitType = limitType,
            issueLimit = issueLimit,
            useLimit = useLimit,
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
                minimumOrderAmount = domain.usageCondition.minimumOrderAmount,
                discountType = domain.discountPolicy.discountType,
                discountAmount = domain.discountPolicy.discountValue,
                issueStartAt = domain.couponPeriod.issueStartAt,
                issueEndAt = domain.couponPeriod.issueEndAt,
                validityDays = domain.couponPeriod.validityDays,
                useEndAt = domain.couponPeriod.useEndAt,
                limitType = domain.usageLimitPolicy.limitType,
                issueLimit = domain.usageLimitPolicy.issueLimit,
                useLimit = domain.usageLimitPolicy.useLimit,
                status = domain.status,
                isIssuable = domain.isIssuable,
                isUsable = domain.isUsable
            )
        }
    }
}
