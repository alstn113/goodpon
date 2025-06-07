package io.github.alstn113.goodpon.application.payment.request

data class AuthenticatePaymentRequest(
    val cardCode: String,
    val token: String,
    val orderId: String,
    val mId: Long
)
