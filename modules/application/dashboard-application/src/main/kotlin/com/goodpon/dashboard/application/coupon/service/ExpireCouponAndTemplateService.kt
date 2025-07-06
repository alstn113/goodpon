package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.ExpireCouponAndTemplateUseCase
import com.goodpon.dashboard.application.coupon.service.accessor.*
import com.goodpon.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.domain.coupon.user.UserCouponStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ExpireCouponAndTemplateService(
    private val userCouponReader: UserCouponReader,
    private val userCouponStore: UserCouponStore,
    private val couponHistoryStore: CouponHistoryStore,
    private val couponTemplateStore: CouponTemplateStore,
    private val couponTemplateReader: CouponTemplateReader,
) : ExpireCouponAndTemplateUseCase {

    @Transactional
    override fun expireExpiredCouponsAndTemplates(now: LocalDateTime) {
        val expirationThreshold = now.toLocalDate().atStartOfDay()

        expireUserCoupons(expirationThreshold, now)
        expireCouponTemplates(expirationThreshold)
    }

    private fun expireUserCoupons(expireCutoff: LocalDateTime, recordedAt: LocalDateTime) {
        val issuedCoupons = userCouponReader.readByStatusAndExpiresAtLessThanEqual(
            status = UserCouponStatus.ISSUED,
            expiresAt = expireCutoff
        )
        issuedCoupons.forEach { coupon ->
            coupon.expire()
            couponHistoryStore.recordExpired(userCouponId = coupon.id, recordedAt = recordedAt)
        }
        userCouponStore.saveAll(issuedCoupons)
    }

    private fun expireCouponTemplates(expireCutoff: LocalDateTime) {
        val issuableTemplates = couponTemplateReader.readByStatusAndAbsoluteExpiresAtLessThanEqual(
            status = CouponTemplateStatus.ISSUABLE,
            absoluteExpiresAt = expireCutoff
        )
        issuableTemplates.forEach { it.expire() }
        couponTemplateStore.saveAll(issuableTemplates)
    }
}