package com.goodpon.dashboard.application.coupon.service.accessor

import com.goodpon.dashboard.application.coupon.port.out.UserCouponRepository
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class UserCouponAccessor(
    private val userCouponRepository: UserCouponRepository,
) {

    @Transactional(readOnly = true)
    fun readByStatusAndExpiresAtLessThanEqual(status: UserCouponStatus, expiresAt: LocalDateTime): List<UserCoupon> {
        return userCouponRepository.findByStatusAndExpiresAtLessThanEqual(status = status, expiresAt = expiresAt)
    }

    @Transactional
    fun saveAll(userCoupons: List<UserCoupon>) {
         userCouponRepository.saveAll(userCoupons)
    }
}