package com.goodpon.partner.application.coupon.service

import com.goodpon.partner.application.coupon.port.`in`.GetUserCouponsUseCase
import com.goodpon.partner.application.coupon.service.dto.UserCouponsView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUserCouponsService : GetUserCouponsUseCase {

    @Transactional(readOnly = true)
    override fun getUserCoupons(merchantId: Long, userId: String): UserCouponsView {
        TODO("Not yet implemented")
    }
}