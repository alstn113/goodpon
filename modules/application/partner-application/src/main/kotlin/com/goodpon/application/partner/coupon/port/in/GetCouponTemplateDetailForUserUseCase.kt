package com.goodpon.application.partner.coupon.port.`in`

import com.goodpon.application.partner.coupon.port.`in`.dto.GetCouponTemplateDetailForUserQuery
import com.goodpon.application.partner.coupon.service.dto.CouponTemplateDetailForUser

fun interface GetCouponTemplateDetailForUserUseCase {

    operator fun invoke(query: GetCouponTemplateDetailForUserQuery): CouponTemplateDetailForUser
}