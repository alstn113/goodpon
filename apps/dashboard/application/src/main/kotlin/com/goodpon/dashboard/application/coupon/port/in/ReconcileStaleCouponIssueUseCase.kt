package com.goodpon.dashboard.application.coupon.port.`in`

import java.time.Duration

fun interface ReconcileStaleCouponIssueUseCase {

    operator fun invoke(olderThan: Duration)
}