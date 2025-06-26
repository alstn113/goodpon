package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.UserCoupon
import com.goodpon.infra.jpa.AuditableEntity
import jakarta.persistence.*
import java.time.LocalDateTime

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
class UserCouponEntity(
    @Id
    val id: String,

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
    val redeemedAt: LocalDateTime?,
) : AuditableEntity() {

    fun toDomain(): UserCoupon {
        return UserCoupon.reconstitute(
            id = id,
            couponTemplateId = couponTemplateId,
            userId = userId,
            issuedAt = issuedAt,
            expiresAt = expiresAt,
            isUsed = isUsed,
            redeemedAt = redeemedAt
        )
    }

    fun update(issuedCoupon: UserCoupon) {}

    companion object {
        fun fromDomain(issuedCoupon: UserCoupon): UserCouponEntity {
            return UserCouponEntity(
                id = issuedCoupon.id,
                couponTemplateId = issuedCoupon.couponTemplateId,
                userId = issuedCoupon.userId,
                issuedAt = issuedCoupon.issuedAt,
                expiresAt = issuedCoupon.expiresAt,
                isUsed = issuedCoupon.isUsed,
                redeemedAt = issuedCoupon.redeemedAt
            )
        }
    }
}