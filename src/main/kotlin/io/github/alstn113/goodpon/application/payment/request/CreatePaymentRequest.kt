package io.github.alstn113.goodpon.application.payment.request

import java.math.BigDecimal

data class CreatePaymentRequest(
    val method: String,
    val amount: Amount,
    val orderId: String,
    val orderName: String,
    val customerEmail: String? = null,
    val customerName: String? = null,
    val mId: Long
)

data class Amount(
    val total: BigDecimal,
    val currency: String
)