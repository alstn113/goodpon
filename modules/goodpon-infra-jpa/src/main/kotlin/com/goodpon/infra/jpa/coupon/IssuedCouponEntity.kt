package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.IssuedCoupon
import com.goodpon.infra.jpa.AuditableEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "issued_coupons")
class IssuedCouponEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    val couponTemplateId: Long,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val issuedAt: LocalDateTime,

    @Column(nullable = true)
    val expiresAt: LocalDateTime?,

    @Column(nullable = false)
    val isUsed: Boolean,

    @Column(nullable = true)
    val usedAt: LocalDateTime?,
) : AuditableEntity() {

    fun toDomain(): IssuedCoupon {
        return IssuedCoupon.reconstitute(
            id = id,
            couponTemplateId = couponTemplateId,
            userId = userId,
            issuedAt = issuedAt,
            expiresAt = expiresAt,
            isUsed = isUsed,
            usedAt = usedAt
        )
    }

    fun update(issuedCoupon: IssuedCoupon) {}

    companion object {
        fun fromDomain(issuedCoupon: IssuedCoupon): IssuedCouponEntity {
            return IssuedCouponEntity(
                id = issuedCoupon.id,
                couponTemplateId = issuedCoupon.couponTemplateId,
                userId = issuedCoupon.userId,
                issuedAt = issuedCoupon.issuedAt,
                expiresAt = issuedCoupon.expiresAt,
                isUsed = issuedCoupon.isUsed,
                usedAt = issuedCoupon.usedAt
            )
        }
    }
}