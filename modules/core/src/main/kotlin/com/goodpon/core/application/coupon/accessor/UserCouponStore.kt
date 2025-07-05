package com.goodpon.core.application.coupon.accessor

import com.goodpon.core.domain.coupon.user.UserCoupon
import com.goodpon.core.domain.coupon.user.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponStore(
    private val userCouponRepository: UserCouponRepository,
) {

    @Transactional
    fun createUserCoupon(userCoupon: UserCoupon): UserCoupon {
        return userCouponRepository.save(userCoupon)
    }

    @Transactional
    fun update(userCoupon: UserCoupon): UserCoupon {
        return userCouponRepository.save(userCoupon)
    }
}