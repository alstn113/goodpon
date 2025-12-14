package com.goodpon.couponissuer.application.port.`in`

fun interface MarkIssueReservationAsFailedUseCase {

    operator fun invoke(couponTemplateId: Long, userId: String)
}