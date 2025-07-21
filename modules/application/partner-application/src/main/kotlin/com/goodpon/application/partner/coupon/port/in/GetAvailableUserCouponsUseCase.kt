package com.goodpon.application.partner.coupon.port.`in`

import com.goodpon.application.partner.coupon.port.`in`.dto.GetAvailableUserCouponsQuery
import com.goodpon.application.partner.coupon.service.dto.AvailableUserCouponsView

fun interface GetAvailableUserCouponsUseCase {

    operator fun invoke(query: GetAvailableUserCouponsQuery): AvailableUserCouponsView
}