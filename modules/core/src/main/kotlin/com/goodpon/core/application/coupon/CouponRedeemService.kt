package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.request.RedeemCouponRequest
import com.goodpon.core.domain.coupon.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponRedeemService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val issuedCouponReader: UserCouponReader,
    private val couponTemplateStatsUpdater: CouponTemplateStatsUpdater,
    private val couponRedeemer: CouponRedeemer,
) {

    @Transactional
    fun redeemCoupon(request: RedeemCouponRequest): CouponRedemptionResult {
        val issuedCoupon = issuedCouponReader.readByIdForUpdate(request.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(issuedCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readByIdForRead(issuedCoupon.couponTemplateId)

        couponTemplate.validateOwnership(request.merchantPrincipal.merchantId)
        issuedCoupon.validateOwnership(request.userId)

        val couponUsageResult = couponRedeemer.redeemCoupon(
            couponTemplate = couponTemplate,
            userCoupon = issuedCoupon,
            redeemCount = stats.redeemCount,
            orderAmount = request.orderAmount,
        )
        couponTemplateStatsUpdater.incrementUsageCount(stats)

        return couponUsageResult
    }
}