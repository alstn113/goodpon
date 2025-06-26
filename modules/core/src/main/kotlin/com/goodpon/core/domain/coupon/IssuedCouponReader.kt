package com.goodpon.core.domain.coupon

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class IssuedCouponReader(
    private val issuedCouponRepository: IssuedCouponRepository,
) {

    @Transactional(readOnly = true)
    fun readByIdForUpdate(id: String): IssuedCoupon {
        return issuedCouponRepository.findByIdForUpdate(id)
            ?: throw IllegalArgumentException("발급된 쿠폰이 존재하지 않습니다.")
    }

    @Transactional(readOnly = true)
    fun readByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): IssuedCoupon? {
        return issuedCouponRepository.findByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }
}