package com.goodpon.dashboard.application.coupon.port.`in`

import java.time.LocalDateTime

fun interface ExpireCouponAndTemplateUseCase {

    operator fun invoke(now: LocalDateTime)
}