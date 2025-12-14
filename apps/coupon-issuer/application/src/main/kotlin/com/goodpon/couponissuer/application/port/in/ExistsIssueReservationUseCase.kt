package com.goodpon.couponissuer.application.port.`in`

fun interface ExistsIssueReservationUseCase {

    operator fun invoke(couponTemplateId: Long, userId: String): Boolean
}