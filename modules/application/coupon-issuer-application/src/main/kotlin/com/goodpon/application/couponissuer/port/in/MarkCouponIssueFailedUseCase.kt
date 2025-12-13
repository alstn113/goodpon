package com.goodpon.application.couponissuer.port.`in`

fun interface MarkCouponIssueFailedUseCase {

    operator fun invoke(couponTemplateId: Long, userId: String)
}