package com.goodpon.core.domain.coupon.service

import com.goodpon.core.domain.coupon.history.CouponActionType
import com.goodpon.core.domain.coupon.history.CouponHistory
import com.goodpon.core.domain.coupon.history.CouponHistoryRepository
import com.goodpon.core.domain.coupon.user.UserCoupon
import com.goodpon.core.domain.coupon.user.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponRedemptionCanceler(
    val couponHistoryRepository: CouponHistoryRepository,
    val userCouponRepository: UserCouponRepository,
) {
    @Transactional
    fun cancelRedemption(
        userCoupon: UserCoupon,
        reason: String,
        cancelAt: LocalDateTime,
    ): CouponCancelRedemptionResult {
        val lastRedeemHistory = getLastRedeemHistory(userCoupon)

        val canceledCoupon = userCoupon.cancelRedemption()
        userCouponRepository.save(canceledCoupon)

        val cancelHistory = CouponHistory.cancelRedemption(
            userCouponId = userCoupon.id,
            orderId = lastRedeemHistory.orderId!!,
            recordedAt = cancelAt,
            reason = reason
        )
        couponHistoryRepository.save(cancelHistory)

        if (canceledCoupon.hasExpired(cancelAt)) {
            val expiredCoupon = canceledCoupon.expire()
            userCouponRepository.save(expiredCoupon)

            val expiredHistory = CouponHistory.expired(
                userCouponId = expiredCoupon.id,
                recordedAt = cancelAt
            )
            couponHistoryRepository.save(expiredHistory)
        }

        return CouponCancelRedemptionResult(
            userCouponId = canceledCoupon.id,
            status = canceledCoupon.status,
            canceledAt = cancelAt,
            cancelReason = reason,
        )
    }

    private fun getLastRedeemHistory(userCoupon: UserCoupon): CouponHistory {
        val histories = couponHistoryRepository.findByUserCouponIdOrderByRecordedAtDesc(userCoupon.id)
        if (histories.isEmpty()) {
            throw IllegalArgumentException("쿠폰 사용 이력이 존재하지 않습니다.")
        }
        val lastHistory = histories.first()
        if (lastHistory.actionType != CouponActionType.REDEEM) {
            throw IllegalArgumentException("마지막 이력이 사용(REDEEM) 상태가 아닙니다.")
        }
        return lastHistory
    }
}