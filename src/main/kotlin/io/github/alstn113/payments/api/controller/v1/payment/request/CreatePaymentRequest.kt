package io.github.alstn113.payments.api.controller.v1.payment.request

import java.math.BigDecimal

data class CreatePaymentRequest(
    val method: String,
    val amount: Amount,
    val orderId: String,
    val orderName: String,
    val customerEmail: String? = null,
    val customerName: String? = null,
)

data class Amount(
    val total: BigDecimal,
    val currency: String
)