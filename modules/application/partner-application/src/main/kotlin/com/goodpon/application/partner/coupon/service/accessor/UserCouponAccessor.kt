package com.goodpon.application.partner.coupon.service.accessor

import com.goodpon.application.partner.coupon.port.out.UserCouponRepository
import com.goodpon.application.partner.coupon.service.exception.UserCouponNotFoundException
import com.goodpon.domain.coupon.user.UserCoupon
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponAccessor(
    private val userCouponRepository: UserCouponRepository,
) {

    @Transactional(readOnly = true)
    fun readByIdForUpdate(id: String): UserCoupon {
        return userCouponRepository.findByIdForUpdate(id)
            ?: throw UserCouponNotFoundException()
    }

    @Transactional(readOnly = true)
    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }

    @Transactional
    fun createUserCoupon(userCoupon: UserCoupon): UserCoupon {
        return userCouponRepository.save(userCoupon)
    }

    @Transactional
    fun update(userCoupon: UserCoupon): UserCoupon {
        return userCouponRepository.save(userCoupon)
    }
}
