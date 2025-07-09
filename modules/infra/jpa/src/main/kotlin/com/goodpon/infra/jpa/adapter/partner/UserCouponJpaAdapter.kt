package com.goodpon.infra.jpa.adapter.partner

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.infra.jpa.core.UserCouponCoreRepository
import com.goodpon.partner.application.coupon.port.out.UserCouponRepository
import org.springframework.stereotype.Repository

@Repository("partnerUserCouponJpaAdapter")
class UserCouponJpaAdapter(
    private val userCouponCoreRepository: UserCouponCoreRepository,
) : UserCouponRepository {

    override fun save(userCoupon: UserCoupon): UserCoupon {
        return userCouponCoreRepository.save(userCoupon)
    }

    override fun findByIdForUpdate(id: String): UserCoupon? {
        return userCouponCoreRepository.findByIdForUpdate(id)
    }

    override fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponCoreRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }
}