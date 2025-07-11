package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.ExpireCouponAndTemplateUseCase
import com.goodpon.dashboard.application.coupon.service.accessor.CouponHistoryAccessor
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.dashboard.application.coupon.service.accessor.UserCouponAccessor
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.coupon.user.UserCouponStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ExpireCouponAndTemplateService(
    private val userCouponAccessor: UserCouponAccessor,
    private val couponHistoryAccessor: CouponHistoryAccessor,
    private val couponTemplateAccessor: CouponTemplateAccessor,
) : ExpireCouponAndTemplateUseCase {

    @Transactional
    override fun expireExpiredCouponsAndTemplates(now: LocalDateTime) {
        val expirationThreshold = now.toLocalDate().atStartOfDay()

        expireUserCoupons(expirationThreshold, now)
        expireCouponTemplates(expirationThreshold)
    }

    private fun expireUserCoupons(expireCutoff: LocalDateTime, recordedAt: LocalDateTime) {
        val issuedCoupons = userCouponAccessor.readByStatusAndExpiresAtLessThanEqual(
            status = UserCouponStatus.ISSUED,
            expiresAt = expireCutoff
        )
        issuedCoupons.forEach { coupon ->
            coupon.expire()
            couponHistoryAccessor.recordExpired(userCouponId = coupon.id, recordedAt = recordedAt)
        }
        userCouponAccessor.saveAll(issuedCoupons)
    }

    private fun expireCouponTemplates(expireCutoff: LocalDateTime) {
        val issuableTemplates = couponTemplateAccessor.readByStatusAndAbsoluteExpiresAtLessThanEqual(
            status = CouponTemplateStatus.ISSUABLE,
            absoluteExpiresAt = expireCutoff
        )
        issuableTemplates.forEach { it.expire() }
        couponTemplateAccessor.saveAll(issuableTemplates)
    }
}