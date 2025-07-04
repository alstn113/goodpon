package com.goodpon.core.application.coupon.accessor

import com.goodpon.core.application.coupon.exception.CouponHistoryLastActionTypeNotRedeemException
import com.goodpon.core.domain.coupon.history.CouponActionType
import com.goodpon.core.domain.coupon.history.CouponHistory
import com.goodpon.core.domain.coupon.history.CouponHistoryRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CouponHistoryReader(
    private val couponHistoryRepository: CouponHistoryRepository,
) {

    @Transactional(readOnly = true)
    fun readLastRedeemHistory(userCouponId: String): CouponHistory {
        return couponHistoryRepository
            .findByUserCouponIdOrderByRecordedAtDesc(userCouponId)
            .firstOrNull()
            ?.takeIf { it.actionType == CouponActionType.REDEEM }
            ?: throw CouponHistoryLastActionTypeNotRedeemException()
    }
}