package com.goodpon.partner.openapi.application

import com.goodpon.partner.application.coupon.service.CancelCouponRedemptionService
import com.goodpon.partner.openapi.support.AbstractIntegrationTest

class CancelCouponRedemptionServiceIT(
    private val cancelCouponRedemptionService: CancelCouponRedemptionService,
) : AbstractIntegrationTest()