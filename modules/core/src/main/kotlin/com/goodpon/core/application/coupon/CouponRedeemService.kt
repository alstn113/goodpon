package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.accessor.CouponTemplateReader
import com.goodpon.core.application.coupon.accessor.CouponTemplateStatsReader
import com.goodpon.core.application.coupon.accessor.CouponTemplateStatsStore
import com.goodpon.core.application.coupon.accessor.UserCouponReader
import com.goodpon.core.application.coupon.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.core.application.coupon.exception.UserCouponNotOwnedByUserException
import com.goodpon.core.application.coupon.request.RedeemCouponRequest
import com.goodpon.core.application.coupon.response.CouponRedemptionResultResponse
import com.goodpon.core.domain.coupon.template.CouponTemplate
import com.goodpon.core.domain.coupon.user.UserCoupon
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
        val now = LocalDateTime.now()

        val userCoupon = userCouponReader.readByIdForUpdate(request.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readByIdForRead(userCoupon.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, request.merchantPrincipal.merchantId)
        validateUserCouponOwnership(userCoupon, request.userId)

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