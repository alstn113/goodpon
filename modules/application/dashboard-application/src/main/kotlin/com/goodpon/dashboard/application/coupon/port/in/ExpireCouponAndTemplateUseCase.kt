package com.goodpon.dashboard.application.coupon.port.`in`

import java.time.LocalDateTime

interface ExpireCouponAndTemplateUseCase {

    fun expireExpiredCouponsAndTemplates(now: LocalDateTime)
}