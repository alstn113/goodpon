package com.goodpon.dashboard.application.coupon.service.accessor

import com.goodpon.dashboard.application.coupon.port.out.UserCouponRepository
import com.goodpon.domain.coupon.user.UserCoupon
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponStore(
    private val userCouponRepository: UserCouponRepository,
) {

    @Transactional
    fun saveAll(userCoupons: List<UserCoupon>): List<UserCoupon> {
        return userCouponRepository.saveAll(userCoupons)
    }
}