package com.goodpon.core.domain.coupon.service

import com.goodpon.core.domain.coupon.history.CouponHistory
import com.goodpon.core.domain.coupon.history.CouponHistoryRepository
import com.goodpon.core.domain.coupon.template.CouponTemplate
import com.goodpon.core.domain.coupon.user.UserCoupon
import com.goodpon.core.domain.coupon.user.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponRedeemer(
    private val userCouponRepository: UserCouponRepository,
    private val couponHistoryRepository: CouponHistoryRepository,
) {
    @Transactional
    fun redeemCoupon(
        couponTemplate: CouponTemplate,
        userCoupon: UserCoupon,
        redeemCount: Long,
        orderAmount: Int,
        orderId: String,
        now: LocalDateTime,
    ): CouponRedemptionResult {
        couponTemplate.validateRedeem(currentRedeemedCount = redeemCount)
            .onFailure { throw it }

        val discountAmount = couponTemplate.calculateDiscountAmount(orderAmount)
        val finalPrice = couponTemplate.calculateFinalPrice(orderAmount)

        val redeemedCoupon = userCoupon.redeem(now = now)
        userCouponRepository.save(redeemedCoupon)
        val history = CouponHistory.redeemed(
            userCouponId = redeemedCoupon.id,
            orderId = orderId,
            now = now
        )
        couponHistoryRepository.save(history)

        return CouponRedemptionResult(
            couponId = userCoupon.id,
            discountAmount = discountAmount,
            originalPrice = orderAmount,
            finalPrice = finalPrice,
            orderId = orderId,
            redeemedAt = now
        )
    }
}
