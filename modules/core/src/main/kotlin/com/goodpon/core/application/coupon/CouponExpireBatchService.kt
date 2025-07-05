package com.goodpon.core.application.coupon

import com.goodpon.core.domain.coupon.template.CouponTemplateRepository
import com.goodpon.core.domain.coupon.template.vo.CouponTemplateStatus
import com.goodpon.core.domain.coupon.user.UserCouponRepository
import com.goodpon.core.domain.coupon.user.UserCouponStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponExpireBatchService(
    private val userCouponRepository: UserCouponRepository,
    private val couponHistoryRecorder: CouponHistoryRecorder,
    private val couponTemplateRepository: CouponTemplateRepository,
) {

    @Transactional
    fun expireCouponsAndTemplates(now: LocalDateTime) {
        val expireLimit = now.toLocalDate().atStartOfDay()

        val couponsToExpire = userCouponRepository.findByStatusAndExpiresAtLessThanEqual(
            status = UserCouponStatus.ISSUED,
            expiresAt = expireLimit
        )

        couponsToExpire.forEach { coupon ->
            coupon.expire()
            couponHistoryRecorder.recordExpired(userCouponId = coupon.id, recordedAt = now)
        }
        userCouponRepository.saveAll(couponsToExpire)

        val templatesToExpire = couponTemplateRepository.findByStatusAndAbsoluteExpiresAtLessThanEqual(
            status = CouponTemplateStatus.ISSUABLE,
            absoluteExpiresAt = expireLimit
        )

        templatesToExpire.forEach { template ->
            template.expire()
        }
        couponTemplateRepository.saveAll(templatesToExpire)
    }
}