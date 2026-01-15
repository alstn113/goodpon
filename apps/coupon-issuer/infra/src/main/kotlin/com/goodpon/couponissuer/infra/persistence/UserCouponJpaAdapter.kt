package com.goodpon.couponissuer.infra.persistence

import com.goodpon.couponissuer.application.port.out.UserCouponRepository
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.infra.jpa.core.UserCouponCoreRepository
import org.springframework.stereotype.Repository

@Repository
class UserCouponJpaAdapter(
    private val userCouponCoreRepository: UserCouponCoreRepository,
) : UserCouponRepository {

    override fun save(userCoupon: UserCoupon): UserCoupon {
        return userCouponCoreRepository.save(userCoupon)
    }

    override fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponCoreRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }

    override fun findAllByUserIdInAndCouponTemplateIdIn(
        userIds: List<String>,
        couponTemplateIds: List<Long>,
    ): List<UserCoupon> {
        return userCouponCoreRepository.findAllByUserIdInAndCouponTemplateIdIn(userIds, couponTemplateIds)
    }

    override fun saveAll(userCoupons: List<UserCoupon>) {
        return userCouponCoreRepository.saveAll(userCoupons)
    }
}