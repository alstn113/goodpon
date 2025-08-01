package com.goodpon.infra.db.jpa.entity

import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.history.CouponHistory
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "coupon_histories")
class CouponHistoryEntity(
    @Id
    @Column(columnDefinition = "VARCHAR(32)")
    val id: String,

    @Column(nullable = false)
    val userCouponId: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val actionType: CouponActionType,

    @Column
    val orderId: String?,

    @Column
    val reason: String?,

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
            reason = reason,
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
                reason = couponHistory.reason,
                recordedAt = couponHistory.recordedAt
            )
        }
    }
}