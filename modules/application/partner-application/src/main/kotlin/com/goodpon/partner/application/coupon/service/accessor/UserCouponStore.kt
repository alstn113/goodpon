package com.goodpon.partner.application.coupon.service.accessor

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.partner.application.coupon.port.out.UserCouponRepository
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