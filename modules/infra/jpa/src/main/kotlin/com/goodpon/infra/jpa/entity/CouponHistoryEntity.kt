package com.goodpon.infra.jpa.entity

import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.history.CouponHistory
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_histories")
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

    fun update(couponHistory: CouponHistory) {}

    fun toDomain(): CouponHistory {
        return CouponHistory.reconstruct(
            id = id,
            userCouponId = userCouponId,
            actionType = actionType,
            orderId = orderId,
            recordedAt = recordedAt
        )
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