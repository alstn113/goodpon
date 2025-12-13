package com.goodpon.application.couponissuer.port.`in`

fun interface HasValidReservationUseCase {

    operator fun invoke(couponTemplateId: Long, userId: String): Boolean
}