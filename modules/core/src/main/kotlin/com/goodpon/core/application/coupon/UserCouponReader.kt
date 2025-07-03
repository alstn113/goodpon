package com.goodpon.core.application.coupon

import com.goodpon.core.domain.coupon.UserCoupon
import com.goodpon.core.domain.coupon.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponReader(
    private val userCouponRepository: UserCouponRepository,
) {
    @Transactional(readOnly = true)
    fun readByIdForUpdate(id: String): UserCoupon {
        return userCouponRepository.findByIdForUpdate(id)
            ?: throw IllegalArgumentException("발급된 쿠폰이 존재하지 않습니다.")
    }

    @Transactional(readOnly = true)
    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }
}