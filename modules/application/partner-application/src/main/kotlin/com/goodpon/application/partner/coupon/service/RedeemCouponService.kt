package com.goodpon.application.partner.coupon.service

import com.goodpon.application.partner.coupon.port.`in`.RedeemCouponUseCase
import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponCommand
import com.goodpon.application.partner.coupon.port.`in`.dto.RedeemCouponResult
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.port.out.dto.RedeemResult
import com.goodpon.application.partner.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.application.partner.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.application.partner.coupon.service.accessor.UserCouponAccessor
import com.goodpon.application.partner.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.application.partner.coupon.service.exception.UserCouponNotOwnedByUserException
import com.goodpon.application.partner.coupon.service.listener.RedeemCouponRollbackEvent
import com.goodpon.domain.coupon.service.CouponRedeemer
import com.goodpon.domain.coupon.template.CouponTemplate
import com.goodpon.domain.coupon.template.exception.CouponTemplateRedemptionLimitExceededException
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.exception.UserCouponAlreadyRedeemedException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RedeemCouponService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val userCouponAccessor: UserCouponAccessor,
    private val couponHistoryAccessor: CouponHistoryAccessor,
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
    private val eventPublisher: ApplicationEventPublisher,
) : RedeemCouponUseCase {

    @Transactional
    override fun invoke(command: RedeemCouponCommand): RedeemCouponResult {
        val now = LocalDateTime.now()

        val (userCoupon, couponTemplate) = getAndValidate(
            userCouponId = command.userCouponId,
            userId = command.userId,
            merchantId = command.merchantId
        )

        val result = CouponRedeemer.redeem(
            couponTemplate = couponTemplate,
            userCoupon = userCoupon,
            orderAmount = command.orderAmount,
            redeemAt = now
        )

        handleRedeemFailure(userCoupon = userCoupon, couponTemplate = couponTemplate)

        val coupon = userCouponAccessor.update(result.redeemedCoupon)
        couponHistoryAccessor.recordRedeemed(userCouponId = coupon.id, recordedAt = now, orderId = command.orderId)

        return RedeemCouponResult(
            userCouponId = coupon.id,
            discountAmount = result.discountAmount,
            originalPrice = result.originalPrice,
            finalPrice = result.finalPrice,
            orderId = command.orderId,
            redeemedAt = now
        )
    }

    private fun getAndValidate(
        userCouponId: String,
        userId: String,
        merchantId: Long,
    ): Pair<UserCoupon, CouponTemplate> {
        val userCoupon = userCouponAccessor.readByIdForUpdate(userCouponId)
        val couponTemplate = couponTemplateAccessor.readById(userCoupon.couponTemplateId)
        validateCouponTemplateOwnership(couponTemplate, merchantId)
        validateUserCouponOwnership(userCoupon, userId)

        return userCoupon to couponTemplate
    }

    private fun handleRedeemFailure(userCoupon: UserCoupon, couponTemplate: CouponTemplate) {
        val redeemResult = couponTemplateStatsCache.redeemCoupon(
            couponTemplateId = couponTemplate.id,
            userId = userCoupon.userId,
            maxRedeemCount = couponTemplate.maxRedeemCount()
        )

        if (redeemResult == RedeemResult.ALREADY_REDEEMED) {
            throw UserCouponAlreadyRedeemedException()
        }
        if (redeemResult == RedeemResult.REDEEM_LIMIT_EXCEEDED) {
            throw CouponTemplateRedemptionLimitExceededException()
        }

        val event = RedeemCouponRollbackEvent(userId = userCoupon.userId, couponTemplateId = couponTemplate.id)
        eventPublisher.publishEvent(event)
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