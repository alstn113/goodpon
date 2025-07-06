package com.goodpon.dashboard.application.coupon.accessor

import com.goodpon.domain.coupon.user.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponStore(
    private val userCouponRepository: UserCouponRepository,
) {

    @Transactional
    fun createUserCoupon(userCoupon: com.goodpon.domain.coupon.user.UserCoupon): com.goodpon.domain.coupon.user.UserCoupon {
        return userCouponRepository.save(userCoupon)
    }

    @Transactional
    fun update(userCoupon: com.goodpon.domain.coupon.user.UserCoupon): com.goodpon.domain.coupon.user.UserCoupon {
        return userCouponRepository.save(userCoupon)
    }
}