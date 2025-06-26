package com.goodpon.core.domain.coupon

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class CouponUser(
    private val issuedCouponRepository: UserCouponRepository,
) {

    @Transactional
    fun useCoupon(
        couponTemplate: CouponTemplate,
        issuedCoupon: UserCoupon,
        usageCount: Long,
        orderAmount: Int,
        now: LocalDateTime = LocalDateTime.now(),
    ): CouponUsageResult {
        couponTemplate.validateUsage(usageCount = usageCount)
            .onFailure { throw it }

        val discountAmount = couponTemplate.calculateDiscountAmount(orderAmount)
        val finalPrice = couponTemplate.calculateFinalPrice(orderAmount)

        val usedCoupon = issuedCoupon.use(now = now)
        issuedCouponRepository.save(usedCoupon)

        return CouponUsageResult(
            couponId = issuedCoupon.id,
            discountAmount = discountAmount,
            originalPrice = orderAmount,
            finalPrice = finalPrice,
            usedAt = now
        )
    }
}
