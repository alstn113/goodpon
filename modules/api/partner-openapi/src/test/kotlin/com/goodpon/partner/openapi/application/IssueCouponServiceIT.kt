package com.goodpon.partner.openapi.application

import com.goodpon.partner.application.coupon.service.IssueCouponService
import com.goodpon.partner.openapi.support.AbstractIntegrationTest

class IssueCouponServiceIT(
    private val issueCouponService: IssueCouponService,
) : AbstractIntegrationTest()
