package com.goodpon.infra.db.jpa.entity

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_coupons",
    uniqueConstraints = [UniqueConstraint(
        name = "uk_user_coupons_user_id_coupon_template_id",
        columnNames = ["user_id", "coupon_template_id"]
    )]
)
class UserCouponEntity(
    @Id
    @Column(columnDefinition = "VARCHAR(32)")
    val id: String,

    @Column(nullable = false)
    val couponTemplateId: Long,

    @Column(nullable = false)
    val userId: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: UserCouponStatus,

    @Column(nullable = false)
    var issuedAt: LocalDateTime,

    @Column(nullable = true)
    var expiresAt: LocalDateTime?,

    @Column(nullable = true)
    var redeemedAt: LocalDateTime?,
) : AuditableEntity() {

    fun update(userCoupon: UserCoupon) {
        this.status = userCoupon.status
        this.issuedAt = userCoupon.issuedAt
        this.expiresAt = userCoupon.expiresAt
        this.redeemedAt = userCoupon.redeemedAt
    }

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