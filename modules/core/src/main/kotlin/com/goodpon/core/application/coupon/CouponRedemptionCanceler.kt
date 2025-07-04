package com.goodpon.core.application.coupon

import com.goodpon.core.domain.coupon.history.CouponActionType
import com.goodpon.core.domain.coupon.history.CouponHistory
import com.goodpon.core.domain.coupon.history.CouponHistoryRepository
import com.goodpon.core.domain.coupon.user.UserCoupon
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponRedemptionCanceler(
    val couponHistoryRepository: CouponHistoryRepository,
    val userCouponUpdater: UserCouponUpdater,
    val couponHistoryRecorder: CouponHistoryRecorder,
) {
    @Transactional
    fun cancelRedemption(
        userCoupon: UserCoupon,
        reason: String,
        cancelAt: LocalDateTime,
    ): CouponCancelRedemptionResult {
        val lastRedeemHistory = getLastRedeemHistory(userCoupon)

        val canceledCoupon = userCouponUpdater.update(userCoupon.cancelRedemption())
        couponHistoryRecorder.recordCancelRedemption(
            userCouponId = canceledCoupon.id,
            orderId = lastRedeemHistory.orderId!!,
            reason = reason,
            recordedAt = cancelAt
        )

        if (canceledCoupon.hasExpired(cancelAt)) {
            val expiredCoupon = userCouponUpdater.update(canceledCoupon.expire())
            couponHistoryRecorder.recordExpired(
                userCouponId = expiredCoupon.id,
                recordedAt = cancelAt
            )
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