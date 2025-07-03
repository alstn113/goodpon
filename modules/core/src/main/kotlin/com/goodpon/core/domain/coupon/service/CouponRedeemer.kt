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
        redeemAt: LocalDateTime,
    ): CouponRedemptionResult {
        couponTemplate.validateRedeem(currentRedeemedCount = redeemCount, orderAmount = orderAmount)

        val discountAmount = couponTemplate.calculateDiscountAmount(orderAmount)
        val finalPrice = couponTemplate.calculateFinalPrice(orderAmount)

        val redeemedCoupon = userCoupon.redeem(redeemAt = redeemAt)
        userCouponRepository.save(redeemedCoupon)
        val history = CouponHistory.redeemed(
            userCouponId = redeemedCoupon.id,
            orderId = orderId,
            recordedAt = redeemAt
        )
        couponHistoryRepository.save(history)

        return CouponRedemptionResult(
            couponId = userCoupon.id,
            discountAmount = discountAmount,
            originalPrice = orderAmount,
            finalPrice = finalPrice,
            orderId = orderId,
            redeemedAt = redeemAt
        )
    }
}
