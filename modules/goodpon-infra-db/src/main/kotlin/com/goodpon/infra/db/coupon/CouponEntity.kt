package com.goodpon.infra.db.coupon

import com.goodpon.core.domain.coupon.Coupon
import com.goodpon.infra.db.AuditableEntity
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "issued_coupons")
@EntityListeners(AuditingEntityListener::class)
class CouponEntity(
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

    fun toDomain(): Coupon {
        return Coupon.reconstitute(
            id = id,
            couponTemplateId = couponTemplateId,
            accountId = accountId,
            issuedAt = issuedAt,
            expiresAt = expiresAt,
            isUsed = isUsed,
            usedAt = usedAt
        )
    }

    fun update(coupon: Coupon) {}

    companion object {
        fun fromDomain(coupon: Coupon): CouponEntity {
            return CouponEntity(
                id = coupon.id,
                couponTemplateId = coupon.couponTemplateId,
                accountId = coupon.accountId,
                issuedAt = coupon.issuedAt,
                expiresAt = coupon.expiresAt,
                isUsed = coupon.isUsed,
                usedAt = coupon.usedAt
            )
        }
    }
}