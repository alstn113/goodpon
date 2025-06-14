package com.goodpon.common.infra.persistence.coupon

import com.goodpon.common.domain.coupon.IssuedCoupon
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.*

@Entity
class IssuedCouponEntity(
    @Id
    val id: UUID,
) {

    companion object {
        fun fromDomain(issuedCoupon: IssuedCoupon): IssuedCouponEntity {
            return IssuedCouponEntity(
                id = issuedCoupon.id,
            )
        }
    }

    fun toDomain(): IssuedCoupon {
        return IssuedCoupon(
            id = id,
            couponTemplate = TODO(),
            accountId = TODO(),
            issuedAt = TODO(),
            expiresAt = TODO(),
            isUsed = TODO(),
            usedAt = TODO(),
            updatedAt = TODO()
        )
    }

    fun update(issuedCoupon: IssuedCoupon) {
        // Update logic here if needed
    }
}