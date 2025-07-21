package com.goodpon.application.dashboard.coupon.port.`in`

import java.time.LocalDateTime

fun interface ExpireCouponAndTemplateUseCase {

    operator fun invoke(now: LocalDateTime)
}