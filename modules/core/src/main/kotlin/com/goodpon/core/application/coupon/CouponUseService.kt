package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.request.UseCouponRequest
import com.goodpon.core.domain.coupon.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponUseService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val issuedCouponReader: UserCouponReader,
    private val couponTemplateStatsUpdater: CouponTemplateStatsUpdater,
    private val couponUser: CouponUser,
) {

    @Transactional
    fun useCoupon(request: UseCouponRequest): CouponUsageResult {
        val issuedCoupon = issuedCouponReader.readByIdForUpdate(request.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(issuedCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readByIdForRead(issuedCoupon.couponTemplateId)

        couponTemplate.validateOwnership(request.merchantPrincipal.merchantId)
        issuedCoupon.validateOwnership(request.userId)

        val couponUsageResult = couponUser.useCoupon(
            couponTemplate = couponTemplate,
            issuedCoupon = issuedCoupon,
            usageCount = stats.usageCount,
            orderAmount = request.orderAmount,
        )
        couponTemplateStatsUpdater.incrementUsageCount(stats)

        return couponUsageResult
    }
}