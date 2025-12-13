package com.goodpon.application.dashboard.coupon.port.`in`

import java.time.Duration

fun interface ReconcileStaleCouponIssueUseCase {

    operator fun invoke(olderThan: Duration)
}