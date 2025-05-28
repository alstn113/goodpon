package io.github.alstn113.payments.api.controller.v1.request

import java.math.BigDecimal

data class PaymentRequestDto(
    val method: String,
    val amount: Amount,
    val orderId: String,
    val orderName: String,
    val successUrl: String,
    val failUrl: String,
    val customerEmail: String? = null,
    val customerName: String? = null,
)

data class Amount(
    val total: BigDecimal,
    val currency: String
)