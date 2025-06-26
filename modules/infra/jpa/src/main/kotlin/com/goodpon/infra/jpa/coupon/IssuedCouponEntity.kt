package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.IssuedCoupon
import com.goodpon.infra.jpa.AuditableEntity
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "issued_coupons",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_issued_coupons_coupon_template_user",
            columnNames = ["coupon_template_id", "user_id"]
        )
    ]
)
class IssuedCouponEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    val couponTemplateId: Long,

    @Column(nullable = false)
    val userId: String,

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