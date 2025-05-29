package io.github.alstn113.payments.api.controller.v1.payment.request

data class AuthenticatePaymentWebRequest(
    val cardCode: String,
    val token: String,
    val orderId: String,
)