package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.request.RedeemCouponRequest
import com.goodpon.core.domain.coupon.service.CouponRedeemer
import com.goodpon.core.domain.coupon.service.CouponRedemptionResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponRedeemService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val userCouponReader: UserCouponReader,
    private val couponTemplateStatsUpdater: CouponTemplateStatsUpdater,
    private val couponRedeemer: CouponRedeemer,
) {
    @Transactional
    fun redeemCoupon(request: RedeemCouponRequest): CouponRedemptionResult {
        val userCoupon = userCouponReader.readByIdForUpdate(request.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readByIdForRead(userCoupon.couponTemplateId)

        couponTemplate.validateOwnership(request.merchantPrincipal.merchantId)
        userCoupon.validateOwnership(request.userId)

        val now = LocalDateTime.now()
        val couponRedemptionResult = couponRedeemer.redeemCoupon(
            couponTemplate = couponTemplate,
            userCoupon = userCoupon,
            redeemCount = stats.redeemCount,
            orderAmount = request.orderAmount,
            orderId = request.orderId,
            now = now
        )
        couponTemplateStatsUpdater.incrementRedeemCount(stats)

        return couponRedemptionResult
    }
}