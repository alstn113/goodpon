package com.goodpon.core.application.coupon

import com.goodpon.core.domain.coupon.*
import com.goodpon.core.domain.coupon.vo.CouponTemplateStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CouponExpireBatchService(
    private val userCouponRepository: UserCouponRepository,
    private val couponHistoryRepository: CouponHistoryRepository,
    private val couponTemplateRepository: CouponTemplateRepository,
) {

    @Transactional
    fun expireCouponsAndTemplates(now: LocalDateTime) {
        val expireLimit = now.toLocalDate().atStartOfDay()

        val couponsToExpire = userCouponRepository.findByStatusAndExpiresAtLessThanEqual(
            status = CouponStatus.ISSUED,
            expiresAt = expireLimit
        )

        couponsToExpire.forEach { coupon ->
            coupon.expire()
            val history = CouponHistory.expired(userCouponId = coupon.id, now = now)
            couponHistoryRepository.save(history)
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