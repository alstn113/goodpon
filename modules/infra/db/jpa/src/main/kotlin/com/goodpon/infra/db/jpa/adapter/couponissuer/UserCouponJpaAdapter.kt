package com.goodpon.infra.db.jpa.adapter.couponissuer

import com.goodpon.application.couponissuer.port.out.UserCouponRepository
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.infra.db.jpa.core.UserCouponCoreRepository
import org.springframework.stereotype.Repository

@Repository("couponIssuerUserCouponJpaAdapter")
class UserCouponJpaAdapter(
    private val userCouponCoreRepository: UserCouponCoreRepository,
) : UserCouponRepository {

    override fun save(userCoupon: UserCoupon): UserCoupon {
        return userCouponCoreRepository.save(userCoupon)
    }

    override fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponCoreRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }
}