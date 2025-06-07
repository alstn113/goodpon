package io.github.alstn113.goodpon.api.controller.v1.payment.request

import java.math.BigDecimal

data class ConfirmPaymentWebRequest(
    val paymentKey: String,
    val orderId: String,
    val amount: BigDecimal,
)
