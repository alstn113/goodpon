package com.goodpon.dashboard.application.coupon

import com.goodpon.dashboard.application.coupon.accessor.CouponTemplateReader
import com.goodpon.dashboard.application.coupon.accessor.CouponTemplateStatsReader
import com.goodpon.dashboard.application.coupon.accessor.CouponTemplateStatsStore
import com.goodpon.dashboard.application.coupon.accessor.UserCouponReader
import com.goodpon.dashboard.application.coupon.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.dashboard.application.coupon.request.CancelCouponRedemptionRequest
import com.goodpon.dashboard.application.coupon.response.CouponCancelRedemptionResultResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponCancelRedemptionService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val userCouponReader: UserCouponReader,
    private val couponTemplateStatsStore: CouponTemplateStatsStore,
    private val couponRedemptionCanceler: CouponRedemptionCanceler,
) {

    @Transactional
    fun cancelCouponRedemption(request: CancelCouponRedemptionRequest): CouponCancelRedemptionResultResponse {
        val now = LocalDateTime.now()

        val userCoupon = userCouponReader.readByIdForUpdate(request.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readById(userCoupon.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, request.merchantId)

        val result = couponRedemptionCanceler.cancelRedemption(
            userCoupon = userCoupon,
            reason = request.cancelReason,
            cancelAt = now
        )
        couponTemplateStatsStore.decrementRedeemCount(stats)

        return CouponCancelRedemptionResultResponse(
            userCouponId = result.id,
            status = result.status,
            canceledAt = now,
            cancelReason = request.cancelReason
        )
    }

    private fun validateCouponTemplateOwnership(
        couponTemplate: com.goodpon.domain.coupon.template.CouponTemplate,
        merchantId: Long,
    ) {
        if (!couponTemplate.isOwnedBy(merchantId)) {
            throw CouponTemplateNotOwnedByMerchantException()
        }
    }
}