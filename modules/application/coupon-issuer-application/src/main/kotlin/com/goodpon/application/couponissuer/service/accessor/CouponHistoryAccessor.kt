package com.goodpon.application.couponissuer.service.accessor

import com.goodpon.application.couponissuer.port.out.CouponHistoryRepository
import com.goodpon.domain.coupon.history.CouponHistory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponHistoryAccessor(
    private val couponHistoryRepository: CouponHistoryRepository,
) {

    @Transactional
    fun recordIssued(userCouponId: String, recordedAt: LocalDateTime): CouponHistory {
        val history = CouponHistory.issued(userCouponId = userCouponId, recordedAt = recordedAt)
        return couponHistoryRepository.save(history)
    }
}