package com.goodpon.core.application.coupon

import com.goodpon.core.domain.UniqueIdGenerator
import com.goodpon.core.domain.coupon.history.CouponHistory
import com.goodpon.core.domain.coupon.history.CouponHistoryRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponHistoryRecorder(
    private val couponHistoryRepository: CouponHistoryRepository,
    private val uniqueIdGenerator: UniqueIdGenerator,
) {

    @Transactional
    fun recordIssued(
        userCouponId: String,
        recordedAt: LocalDateTime,
    ): CouponHistory {
        val history = CouponHistory.issued(
            id = uniqueIdGenerator.generate(),
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
            id = uniqueIdGenerator.generate(),
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
            id = uniqueIdGenerator.generate(),
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
            id = uniqueIdGenerator.generate(),
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
            id = uniqueIdGenerator.generate(),
            userCouponId = userCouponId,
            recordedAt = recordedAt,
            reason = reason
        )
        return couponHistoryRepository.save(history)
    }
}