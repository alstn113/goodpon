package com.goodpon.application.partner.coupon.service

import com.goodpon.application.partner.coupon.port.`in`.RedeemCouponUseCase
import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponResult
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.partner.coupon.service.accessor.UserCouponAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.partner.coupon.service.exception.UserCouponNotOwnedByUserException
import com.goodpon.domain.coupon.service.CouponRedeemer
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.exception.CouponTemplateRedemptionLimitExceededException
import com.goodpon.domain.coupon.user.UserCoupon
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RedeemCouponService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
    private val userCouponAccessor: UserCouponAccessor,
    private val couponHistoryAccessor: CouponHistoryAccessor,
) : RedeemCouponUseCase {

    @Transactional
    override fun invoke(command: RedeemCouponCommand): RedeemCouponResult {
        val now = LocalDateTime.now()

        val userCoupon = userCouponAccessor.readByIdForUpdate(command.userCouponId)
        val couponTemplate = couponTemplateAccessor.readById(userCoupon.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, command.merchantId)
        validateUserCouponOwnership(userCoupon, command.userId)

        val result = CouponRedeemer.redeem(
            couponTemplate = couponTemplate,
            userCoupon = userCoupon,
            orderAmount = command.orderAmount,
            redeemAt = now
        )
        val redeemedCoupon = userCouponAccessor.update(result.redeemedCoupon)
        couponHistoryAccessor.recordRedeemed(
            userCouponId = redeemedCoupon.id,
            recordedAt = now,
            orderId = command.orderId
        )

        val incremented = couponTemplateStatsCache.incrementRedeemCount(
            couponTemplateId = couponTemplate.id,
            limit = couponTemplate.maxRedeemCount(),
        )
        if (!incremented) {
            throw CouponTemplateRedemptionLimitExceededException()
        }

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