package com.goodpon.domain.application.coupon

import com.goodpon.domain.application.coupon.accessor.*
import com.goodpon.domain.application.coupon.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.domain.application.coupon.exception.UserCouponNotOwnedByUserException
import com.goodpon.domain.application.coupon.request.RedeemCouponRequest
import com.goodpon.domain.application.coupon.response.CouponRedemptionResultResponse
import com.goodpon.domain.domain.coupon.service.CouponRedeemer
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.user.UserCoupon
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponRedeemService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val userCouponReader: UserCouponReader,
    private val couponTemplateStatsStore: CouponTemplateStatsStore,
    private val userCouponStore: UserCouponStore,
    private val couponHistoryStore: CouponHistoryStore,
) {

    @Transactional
    fun redeemCoupon(request: RedeemCouponRequest): CouponRedemptionResultResponse {
        val now = LocalDateTime.now()

        val userCoupon = userCouponReader.readByIdForUpdate(request.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readById(userCoupon.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, request.merchantId)
        validateUserCouponOwnership(userCoupon, request.userId)

        val result = CouponRedeemer.redeem(
            couponTemplate = couponTemplate,
            userCoupon = userCoupon,
            currentRedeemCount = stats.redeemCount,
            orderAmount = request.orderAmount,
            redeemAt = now
        )
        val redeemedCoupon = userCouponStore.update(result.redeemedCoupon)

        couponHistoryStore.recordRedeemed(
            userCouponId = redeemedCoupon.id,
            recordedAt = now,
            orderId = request.orderId,
        )
        couponTemplateStatsStore.incrementRedeemCount(stats)

        return CouponRedemptionResultResponse(
            couponId = redeemedCoupon.id,
            discountAmount = result.discountAmount,
            originalPrice = result.originalPrice,
            finalPrice = result.finalPrice,
            orderId = request.orderId,
            redeemedAt = now
        )
    }

    private fun validateCouponTemplateOwnership(couponTemplate: com.goodpon.domain.coupon.template.CouponTemplate, merchantId: Long) {
        if (!couponTemplate.isOwnedBy(merchantId)) {
            throw CouponTemplateNotOwnedByMerchantException()
        }
    }

    private fun validateUserCouponOwnership(userCoupon: com.goodpon.domain.coupon.user.UserCoupon, userId: String) {
        if (!userCoupon.isOwnedBy(userId)) {
            throw UserCouponNotOwnedByUserException()
        }
    }
}