package io.github.alstn113.payments.application.payment.request

data class ConfirmPaymentRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: String,
    val mId: Long
)
