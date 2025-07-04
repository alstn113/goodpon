package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.accessor.CouponTemplateReader
import com.goodpon.core.application.coupon.accessor.CouponTemplateStatsReader
import com.goodpon.core.application.coupon.accessor.CouponTemplateStatsStore
import com.goodpon.core.application.coupon.accessor.UserCouponReader
import com.goodpon.core.application.coupon.request.RedeemCouponRequest
import com.goodpon.core.application.coupon.response.CouponRedemptionResultResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponRedeemService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val userCouponReader: UserCouponReader,
    private val couponTemplateStatsStore: CouponTemplateStatsStore,
    private val couponRedeemer: CouponRedeemer,
) {
    @Transactional
    fun redeemCoupon(request: RedeemCouponRequest): CouponRedemptionResultResponse {
        val userCoupon = userCouponReader.readByIdForUpdate(request.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readByIdForRead(userCoupon.couponTemplateId)

        if (!couponTemplate.isOwnedBy(request.merchantPrincipal.merchantId)) {
            throw IllegalArgumentException("쿠폰 템플릿이 소유자와 일치하지 않습니다.")
        }
        if (!userCoupon.isOwnedBy(request.userId)) {
            throw IllegalArgumentException("쿠폰이 사용자와 일치하지 않습니다.")
        }

        val now = LocalDateTime.now()
        val couponRedemptionResult = couponRedeemer.redeemCoupon(
            couponTemplate = couponTemplate,
            userCoupon = userCoupon,
            redeemCount = stats.redeemCount,
            orderAmount = request.orderAmount,
            orderId = request.orderId,
            redeemAt = now
        )
        couponTemplateStatsStore.incrementRedeemCount(stats)

        return couponRedemptionResult
    }
}