package com.goodpon.infra.db.coupon

import com.goodpon.core.domain.coupon.IssuedCoupon
import com.goodpon.infra.db.AuditableEntity
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "issued_coupons")
@EntityListeners(AuditingEntityListener::class)
class IssuedCouponEntity(
    @Id
    val id: UUID,

    @Column(nullable = false)
    val couponTemplateId: Long,

    @Column(nullable = false)
    val accountId: Long,

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
            accountId = accountId,
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
                accountId = issuedCoupon.accountId,
                issuedAt = issuedCoupon.issuedAt,
                expiresAt = issuedCoupon.expiresAt,
                isUsed = issuedCoupon.isUsed,
                usedAt = issuedCoupon.usedAt
            )
        }
    }
}