package com.goodpon.partner.application.coupon.service

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.partner.application.coupon.port.`in`.CancelCouponRedemptionUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionResult
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateStatsAccessor
import com.goodpon.partner.application.coupon.service.accessor.UserCouponAccessor
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CancelCouponRedemptionService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val couponTemplateStatsAccessor: CouponTemplateStatsAccessor,
    private val userCouponAccessor: UserCouponAccessor,
    private val couponRedemptionCancelProcessor: CouponRedemptionCancelProcessor,
) : CancelCouponRedemptionUseCase {

    @Transactional
    override fun invoke(command: CancelCouponRedemptionCommand): CancelCouponRedemptionResult {
        val now = LocalDateTime.now()

        val userCoupon = userCouponAccessor.readByIdForUpdate(command.userCouponId)
        val stats = couponTemplateStatsAccessor.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateAccessor.readById(userCoupon.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, command.merchantId)

        val canceledCoupon = couponRedemptionCancelProcessor.process(
            userCoupon = userCoupon,
            orderId = command.orderId,
            cancelReason = command.cancelReason,
            cancelAt = now
        )
        couponTemplateStatsAccessor.decrementRedeemCount(stats)

        return CancelCouponRedemptionResult(
            userCouponId = canceledCoupon.id,
            status = canceledCoupon.status,
            canceledAt = now,
            cancelReason = command.cancelReason
        )
    }

    private fun validateCouponTemplateOwnership(couponTemplate: CouponTemplate, merchantId: Long) {
        if (!couponTemplate.isOwnedBy(merchantId)) {
            throw CouponTemplateNotOwnedByMerchantException()
        }
    }
}