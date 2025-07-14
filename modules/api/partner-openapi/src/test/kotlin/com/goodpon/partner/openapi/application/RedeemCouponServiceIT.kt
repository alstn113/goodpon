package com.goodpon.partner.openapi.application

import com.goodpon.partner.application.coupon.service.RedeemCouponService
import com.goodpon.partner.openapi.support.AbstractIntegrationTest

class RedeemCouponServiceIT(
    private val redeemCouponService: RedeemCouponService,
) : AbstractIntegrationTest()
