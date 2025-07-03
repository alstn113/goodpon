package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.request.CancelCouponRedemptionRequest
import com.goodpon.core.domain.coupon.service.CouponCancelRedemptionResult
import com.goodpon.core.domain.coupon.service.CouponRedemptionCanceler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponCancelRedemptionService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val userCouponReader: UserCouponReader,
    private val couponTemplateStatsUpdater: CouponTemplateStatsUpdater,
    private val couponRedemptionCanceler: CouponRedemptionCanceler,
) {
    @Transactional
    fun cancelCouponRedemption(request: CancelCouponRedemptionRequest): CouponCancelRedemptionResult {
        val userCoupon = userCouponReader.readByIdForUpdate(request.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readByIdForRead(userCoupon.couponTemplateId)

        if (!couponTemplate.isOwnedBy(request.merchantPrincipal.merchantId)) {
            throw IllegalArgumentException("쿠폰 템플릿이 소유자와 일치하지 않습니다.")
        }

        val now = LocalDateTime.now()
        val couponCancelRedemptionResult = couponRedemptionCanceler.cancelRedemption(
            userCoupon = userCoupon,
            reason = request.cancelReason,
            cancelAt = now
        )
        couponTemplateStatsUpdater.decrementRedeemCount(stats)

        return couponCancelRedemptionResult
    }
}