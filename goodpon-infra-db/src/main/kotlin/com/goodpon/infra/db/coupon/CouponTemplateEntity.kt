package com.goodpon.infra.db.coupon

import com.goodpon.core.domain.coupon.CouponTemplate
import com.goodpon.core.domain.coupon.CouponTemplateLimitType
import com.goodpon.core.domain.coupon.CouponTemplateStatus
import com.goodpon.core.domain.coupon.DiscountType
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

    companion object {

        fun fromDomain(domain: CouponTemplate): CouponTemplateEntity {
            return CouponTemplateEntity(
            )
        }
    }

    fun toDomain(): CouponTemplate {
        return CouponTemplate(
            id = id,
            merchantId = TODO(),
            name = TODO(),
            description = TODO(),
            usageCondition = TODO(),
            discountPolicy = TODO(),
            couponPeriod = TODO(),
            usageLimitPolicy = TODO(),
            status = TODO(),
            isIssuable = TODO(),
            isUsable = TODO(),
        )
    }

    fun update(couponTemplate: CouponTemplate) {

    }
}
