package io.github.alstn113.goodpon.application.payment.request

import java.math.BigDecimal

data class ConfirmPaymentRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: BigDecimal,
    val mId: Long,
)
