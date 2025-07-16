package com.goodpon.partner.application.coupon.service

import com.goodpon.domain.coupon.service.CouponRedeemer
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.partner.application.coupon.port.`in`.RedeemCouponUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponResult
import com.goodpon.partner.application.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateStatsAccessor
import com.goodpon.partner.application.coupon.service.accessor.UserCouponAccessor
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.partner.application.coupon.service.exception.UserCouponNotOwnedByUserException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime

@Service
class RedeemCouponService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val couponTemplateStatsAccessor: CouponTemplateStatsAccessor,
    private val userCouponAccessor: UserCouponAccessor,
    private val couponHistoryAccessor: CouponHistoryAccessor,
    private val clock: Clock,
) : RedeemCouponUseCase {

    @Transactional
    override fun invoke(command: RedeemCouponCommand): RedeemCouponResult {
        val now = LocalDateTime.now(clock)

        val userCoupon = userCouponAccessor.readByIdForUpdate(command.userCouponId)
        val stats = couponTemplateStatsAccessor.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateAccessor.readById(userCoupon.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, command.merchantId)
        validateUserCouponOwnership(userCoupon, command.userId)

        val result = CouponRedeemer.redeem(
            couponTemplate = couponTemplate,
            userCoupon = userCoupon,
            currentRedeemCount = stats.redeemCount,
            orderAmount = command.orderAmount,
            redeemAt = now
        )
        val redeemedCoupon = userCouponAccessor.update(result.redeemedCoupon)

        couponHistoryAccessor.recordRedeemed(
            userCouponId = redeemedCoupon.id,
            recordedAt = now,
            orderId = command.orderId
        )
        couponTemplateStatsAccessor.incrementRedeemCount(stats)

        return RedeemCouponResult(
            userCouponId = redeemedCoupon.id,
            discountAmount = result.discountAmount,
            originalPrice = result.originalPrice,
            finalPrice = result.finalPrice,
            orderId = command.orderId,
            redeemedAt = now
        )
    }

    private fun validateCouponTemplateOwnership(couponTemplate: CouponTemplate, merchantId: Long) {
        if (!couponTemplate.isOwnedBy(merchantId)) {
            throw CouponTemplateNotOwnedByMerchantException()
        }
    }

    private fun validateUserCouponOwnership(userCoupon: UserCoupon, userId: String) {
        if (!userCoupon.isOwnedBy(userId)) {
            throw UserCouponNotOwnedByUserException()
        }
    }
}