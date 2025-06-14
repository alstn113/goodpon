package com.goodpon.infra.db.coupon

import com.goodpon.core.domain.coupon.CouponTemplate
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class CouponTemplateEntity {

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
            usageLimit = TODO(),
            status = TODO(),
            isIssuable = TODO(),
            isUsable = TODO(),
            createdAt = TODO(),
            updatedAt = TODO(),
        )
    }

    fun update(couponTemplate: CouponTemplate) {

    }
}
