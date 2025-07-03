package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.CouponStatus
import com.goodpon.core.domain.coupon.UserCoupon
import com.goodpon.infra.jpa.AuditableEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_coupons",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_user_coupons_coupon_template_user",
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
    @Enumerated(EnumType.STRING)
    var status: CouponStatus,

    @Column(nullable = false)
    var issuedAt: LocalDateTime,

    @Column(nullable = true)
    var expiresAt: LocalDateTime?,

    @Column(nullable = true)
    var redeemedAt: LocalDateTime?,
) : AuditableEntity() {

    fun toDomain(): UserCoupon {
        return UserCoupon.reconstruct(
            id = id,
            couponTemplateId = couponTemplateId,
            userId = userId,
            status = status,
            issuedAt = issuedAt,
            expiresAt = expiresAt,
            redeemedAt = redeemedAt,
        )
    }

    fun update(userCoupon: UserCoupon) {
        this.status = userCoupon.status
        this.issuedAt = userCoupon.issuedAt
        this.expiresAt = userCoupon.expiresAt
        this.redeemedAt = userCoupon.redeemedAt
    }

    companion object {
        fun fromDomain(userCoupon: UserCoupon): UserCouponEntity {
            return UserCouponEntity(
                id = userCoupon.id,
                couponTemplateId = userCoupon.couponTemplateId,
                userId = userCoupon.userId,
                status = userCoupon.status,
                issuedAt = userCoupon.issuedAt,
                expiresAt = userCoupon.expiresAt,
                redeemedAt = userCoupon.redeemedAt
            )
        }
    }
}