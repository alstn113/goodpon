package com.goodpon.application.partner.coupon.service

import com.goodpon.application.partner.coupon.port.`in`.GetAvailableUserCouponsUseCase
import com.goodpon.application.partner.coupon.port.`in`.dto.GetAvailableUserCouponsQuery
import com.goodpon.application.partner.coupon.port.out.UserCouponRepository
import com.goodpon.application.partner.coupon.service.dto.AvailableUserCouponsView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetAvailableUserCouponsService(
    private val userCouponRepository: UserCouponRepository,
) : GetAvailableUserCouponsUseCase {

    @Transactional(readOnly = true)
    override fun invoke(query: GetAvailableUserCouponsQuery): AvailableUserCouponsView {
        val coupons = userCouponRepository.findAvailableUserCouponsView(
            userId = query.userId,
            merchantId = query.merchantId,
            orderAmount = query.orderAmount
        )

        return AvailableUserCouponsView(coupons = coupons)
    }
}