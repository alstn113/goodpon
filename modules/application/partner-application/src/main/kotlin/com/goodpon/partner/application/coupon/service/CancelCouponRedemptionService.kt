package com.goodpon.partner.application.coupon.service

import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.partner.application.coupon.port.`in`.CancelCouponRedemptionUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionCommand
import com.goodpon.partner.application.coupon.port.`in`.dto.CancelCouponRedemptionResult
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateReader
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateStatsReader
import com.goodpon.partner.application.coupon.service.accessor.CouponTemplateStatsStore
import com.goodpon.partner.application.coupon.service.accessor.UserCouponReader
import com.goodpon.partner.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CancelCouponRedemptionService(
    private val couponTemplateReader: CouponTemplateReader,
    private val couponTemplateStatsReader: CouponTemplateStatsReader,
    private val userCouponReader: UserCouponReader,
    private val couponTemplateStatsStore: CouponTemplateStatsStore,
    private val couponRedemptionCancelProcessor: CouponRedemptionCancelProcessor,
) : CancelCouponRedemptionUseCase {

    @Transactional
    override fun cancelCouponRedemption(command: CancelCouponRedemptionCommand): CancelCouponRedemptionResult {
        val now = LocalDateTime.now()

        val userCoupon = userCouponReader.readByIdForUpdate(command.couponId)
        val stats = couponTemplateStatsReader.readByCouponTemplateIdForUpdate(userCoupon.couponTemplateId)
        val couponTemplate = couponTemplateReader.readById(userCoupon.couponTemplateId)

        validateCouponTemplateOwnership(couponTemplate, command.merchantId)

        val canceledCoupon = couponRedemptionCancelProcessor.process(
            userCoupon = userCoupon,
            reason = command.cancelReason,
            cancelAt = now
        )
        couponTemplateStatsStore.decrementRedeemCount(stats)

        return CancelCouponRedemptionResult(
            userCouponId = canceledCoupon.id,
            status = canceledCoupon.status,
            canceledAt = now,
            cancelReason = command.cancelReason
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
}