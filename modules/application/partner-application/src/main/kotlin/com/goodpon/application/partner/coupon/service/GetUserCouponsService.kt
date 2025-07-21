package com.goodpon.application.partner.coupon.service

import com.goodpon.application.partner.coupon.port.`in`.GetUserCouponsUseCase
import com.goodpon.application.partner.coupon.port.out.UserCouponRepository
import com.goodpon.application.partner.coupon.service.dto.UserCouponsView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUserCouponsService(
    private val userCouponRepository: UserCouponRepository,
) : GetUserCouponsUseCase {

    @Transactional(readOnly = true)
    override fun invoke(merchantId: Long, userId: String): UserCouponsView {
        val coupons = userCouponRepository.findUserCouponsView(
            userId = userId,
            merchantId = merchantId
        )

        return UserCouponsView(coupons = coupons)
    }
}