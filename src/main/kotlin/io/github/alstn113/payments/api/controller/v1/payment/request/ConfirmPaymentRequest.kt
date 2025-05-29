package io.github.alstn113.payments.api.controller.v1.payment.request

data class ConfirmPaymentRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: String
)
