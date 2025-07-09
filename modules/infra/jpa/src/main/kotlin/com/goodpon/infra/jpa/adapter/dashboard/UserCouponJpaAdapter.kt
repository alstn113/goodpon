package com.goodpon.infra.jpa.adapter.dashboard

import com.goodpon.dashboard.application.coupon.port.out.UserCouponRepository
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.jpa.core.UserCouponCoreRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UserCouponJpaAdapter(
    private val userCouponCoreRepository: UserCouponCoreRepository,
) : UserCouponRepository {

    override fun saveAll(userCoupons: List<UserCoupon>): List<UserCoupon> {
        return userCouponCoreRepository.saveAll(userCoupons)
    }

    override fun findByStatusAndExpiresAtLessThanEqual(
        status: UserCouponStatus,
        expiresAt: LocalDateTime,
    ): List<UserCoupon> {
        return userCouponCoreRepository.findByStatusAndExpiresAtLessThanEqual(status, expiresAt)
    }
}