package com.goodpon.dashboard.application.coupon.accessor

import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.domain.coupon.history.CouponHistoryRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponHistoryStore(
    private val couponHistoryRepository: CouponHistoryRepository,
) {

    @Transactional
    fun recordIssued(
        userCouponId: String,
        recordedAt: LocalDateTime,
    ): CouponHistory {
        val history = CouponHistory.issued(
            userCouponId = userCouponId,
            recordedAt = recordedAt
        )
        return couponHistoryRepository.save(history)
    }

    @Transactional
    fun recordRedeemed(
        userCouponId: String,
        orderId: String,
        recordedAt: LocalDateTime,
    ): CouponHistory {
        val history = CouponHistory.redeemed(
            userCouponId = userCouponId,
            orderId = orderId,
            recordedAt = recordedAt
        )
        return couponHistoryRepository.save(history)
    }

    @Transactional
    fun recordCancelRedemption(
        userCouponId: String,
        orderId: String,
        reason: String,
        recordedAt: LocalDateTime,
    ): CouponHistory {
        val history = CouponHistory.cancelRedemption(
            userCouponId = userCouponId,
            orderId = orderId,
            reason = reason,
            recordedAt = recordedAt
        )
        return couponHistoryRepository.save(history)
    }

    @Transactional
    fun recordExpired(
        userCouponId: String,
        recordedAt: LocalDateTime,
    ): CouponHistory {
        val history = CouponHistory.expired(
            userCouponId = userCouponId,
            recordedAt = recordedAt
        )
        return couponHistoryRepository.save(history)
    }

    @Transactional
    fun recordDiscarded(
        userCouponId: String,
        recordedAt: LocalDateTime,
        reason: String,
    ): CouponHistory {
        val history = CouponHistory.discarded(
            userCouponId = userCouponId,
            recordedAt = recordedAt,
            reason = reason
        )
        return couponHistoryRepository.save(history)
    }
}