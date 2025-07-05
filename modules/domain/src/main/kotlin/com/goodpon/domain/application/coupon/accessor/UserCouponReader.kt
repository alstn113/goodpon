package com.goodpon.domain.application.coupon.accessor

import com.goodpon.domain.application.coupon.exception.UserCouponNotFoundException
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.domain.coupon.user.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponReader(
    private val userCouponRepository: UserCouponRepository,
) {

    @Transactional(readOnly = true)
    fun readByIdForUpdate(id: String): com.goodpon.domain.coupon.user.UserCoupon {
        return userCouponRepository.findByIdForUpdate(id)
            ?: throw UserCouponNotFoundException()
    }

    @Transactional(readOnly = true)
    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }
}