package com.goodpon.partner.application.coupon.service.accessor

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.partner.application.coupon.port.out.UserCouponRepository
import com.goodpon.partner.application.coupon.service.exception.UserCouponNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponReader(
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
}