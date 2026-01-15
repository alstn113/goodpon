package com.goodpon.couponissuer.application.service.accessor

import com.goodpon.couponissuer.application.port.out.UserCouponRepository
import com.goodpon.domain.coupon.user.UserCoupon
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UserCouponAccessor(
    private val userCouponRepository: UserCouponRepository,
) {

    @Transactional(readOnly = true)
    fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }

    @Transactional
    fun save(userCoupon: UserCoupon): UserCoupon {
        return userCouponRepository.save(userCoupon)
    }

    @Transactional(readOnly = true)
    fun readAllByUserIdInAndTemplateIdIn(userIds: List<String>, couponTemplateIds: List<Long>): List<UserCoupon> {
        return userCouponRepository.findAllByUserIdInAndCouponTemplateIdIn(userIds, couponTemplateIds)
    }

    @Transactional
    fun bulkInsertCoupons(userCoupons: List<UserCoupon>) {
        userCouponRepository.saveAll(userCoupons)
    }
}