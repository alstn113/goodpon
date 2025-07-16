package com.goodpon.partner.application.coupon.service

import com.goodpon.partner.application.coupon.port.`in`.GetCouponTemplateStatusesUseCase
import com.goodpon.partner.application.coupon.service.dto.CouponTemplateStatusesView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetCouponTemplateStatusesService : GetCouponTemplateStatusesUseCase {

    @Transactional(readOnly = true)
    override fun getCouponTemplateStatuses(
        merchantId: Long,
        couponTemplateId: Long,
        userId: String?,
    ): CouponTemplateStatusesView {
        TODO("Not yet implemented")
    }
}
