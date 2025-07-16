package com.goodpon.partner.application.coupon.service.accessor

import com.goodpon.domain.coupon.history.CouponHistory
import com.goodpon.partner.application.coupon.port.out.CouponHistoryRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponHistoryAccessor(
    private val couponHistoryRepository: CouponHistoryRepository,
) {

    @Transactional(readOnly = true)
    fun readLastHistory(userCouponId: String): CouponHistory? {
        return couponHistoryRepository
            .findByUserCouponIdOrderByRecordedAtDesc(userCouponId)
            .firstOrNull()
    }

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
}