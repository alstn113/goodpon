package com.goodpon.dashboard.application.coupon.service.accessor

import com.goodpon.dashboard.application.coupon.port.out.CouponHistoryRepository
import com.goodpon.domain.coupon.history.CouponHistory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponHistoryStore(
    private val couponHistoryRepository: CouponHistoryRepository,
) {

    @Transactional
    fun recordExpired(userCouponId: String, recordedAt: LocalDateTime): CouponHistory {
        val history = CouponHistory.expired(
            userCouponId = userCouponId,
            recordedAt = recordedAt
        )
        return couponHistoryRepository.save(history)
    }
}