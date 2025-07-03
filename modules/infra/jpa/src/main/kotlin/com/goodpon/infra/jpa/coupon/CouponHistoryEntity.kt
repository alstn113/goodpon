package com.goodpon.infra.jpa.coupon

import com.goodpon.core.domain.coupon.history.CouponActionType
import com.goodpon.core.domain.coupon.history.CouponHistory
import com.goodpon.infra.jpa.AuditableEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class CouponHistoryEntity(
    @Id
    val id: String,

    @Column(nullable = false)
    val userCouponId: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val actionType: CouponActionType,

    @Column
    val orderId: String?,

    @Column(nullable = false)
    val recordedAt: LocalDateTime,
) : AuditableEntity() {
    fun toDomain(): CouponHistory {
        return CouponHistory.reconstruct(
            id = id,
            userCouponId = userCouponId,
            actionType = actionType,
            orderId = orderId,
            recordedAt = recordedAt
        )
    }

    fun update(couponHistory: CouponHistory) {
    }

    companion object {
        fun fromDomain(couponHistory: CouponHistory): CouponHistoryEntity {
            return CouponHistoryEntity(
                id = couponHistory.id,
                userCouponId = couponHistory.userCouponId,
                actionType = couponHistory.actionType,
                orderId = couponHistory.orderId,
                recordedAt = couponHistory.recordedAt
            )
        }
    }
}