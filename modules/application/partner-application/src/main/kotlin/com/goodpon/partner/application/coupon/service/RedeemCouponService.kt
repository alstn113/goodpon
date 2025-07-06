package com.goodpon.partner.application.coupon.service

import com.goodpon.domain.coupon.service.CouponRedeemer
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.partner.application.coupon.port.`in`.RedeemCouponUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.RedeemCouponResult
import com.goodpon.partner.application.coupon.service.accessor.*
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.partner.application.coupon.service.exception.UserCouponNotOwnedByUserException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RedeemCouponService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val userCouponReader: UserCouponReader,
    private val couponTemplateStatsStore: CouponTemplateStatsStore,
    private val userCouponStore: UserCouponStore,
    private val couponHistoryStore: CouponHistoryStore,
) : RedeemCouponUseCase {

    @Transactional
    override fun redeemCoupon(command: RedeemCouponCommand): RedeemCouponResult {
        val now = LocalDateTime.now()

        val userCoupon = userCouponReader.readByIdForUpdate(command.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readById(userCoupon.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, command.merchantId)
        validateUserCouponOwnership(userCoupon, command.userId)

        val result = CouponRedeemer.redeem(
            couponTemplate = couponTemplate,
            userCoupon = userCoupon,
            currentRedeemCount = stats.redeemCount,
            orderAmount = command.orderAmount,
            redeemAt = now
        )
        val redeemedCoupon = userCouponStore.update(result.redeemedCoupon)

        couponHistoryStore.recordRedeemed(
            userCouponId = redeemedCoupon.id,
            recordedAt = now,
            orderId = command.orderId,
        )
        couponTemplateStatsStore.incrementRedeemCount(stats)

        return RedeemCouponResult(
            couponId = redeemedCoupon.id,
            discountAmount = result.discountAmount,
            originalPrice = result.originalPrice,
            finalPrice = result.finalPrice,
            orderId = command.orderId,
            redeemedAt = now
        )
    }

    private fun validateCouponTemplateOwnership(
        couponTemplate: CouponTemplate,
        merchantId: Long,
    ) {
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