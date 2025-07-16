package com.goodpon.partner.application.coupon.port.`in`

import com.goodpon.partner.application.coupon.port.`in`.dto.GetCouponTemplateDetailForUserQuery
import com.goodpon.partner.application.coupon.service.dto.CouponTemplateDetailForUser

fun interface GetCouponTemplateDetailForUserUseCase {

    operator fun invoke(query: GetCouponTemplateDetailForUserQuery): CouponTemplateDetailForUser
}