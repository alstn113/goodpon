package com.goodpon.core.domain.coupon

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponRedeemer(
    private val issuedCouponRepository: UserCouponRepository,
) {

    @Transactional
    fun redeemCoupon(
        couponTemplate: CouponTemplate,
        userCoupon: UserCoupon,
        redeemCount: Long,
        orderAmount: Int,
        now: LocalDateTime = LocalDateTime.now(),
    ): CouponRedemptionResult {
        couponTemplate.validateUsage(redeemCount = redeemCount)
            .onFailure { throw it }

        val discountAmount = couponTemplate.calculateDiscountAmount(orderAmount)
        val finalPrice = couponTemplate.calculateFinalPrice(orderAmount)

        val redeemedCoupon = userCoupon.redeem(now = now)
        issuedCouponRepository.save(redeemedCoupon)

        return CouponRedemptionResult(
            couponId = userCoupon.id,
            discountAmount = discountAmount,
            originalPrice = orderAmount,
            finalPrice = finalPrice,
            redeemedAt = now
        )
    }
}
