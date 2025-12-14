package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.port.`in`.dto.GetAvailableUserCouponsQuery
import com.goodpon.partner.application.coupon.service.dto.AvailableUserCouponsView

fun interface GetAvailableUserCouponsUseCase {

    operator fun invoke(query: GetAvailableUserCouponsQuery): AvailableUserCouponsView
}