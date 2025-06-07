package io.github.alstn113.goodpon.domain.transaction

import io.github.alstn113.goodpon.domain.payment.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class Transaction(
    val id: String?,
    val mid: String,
    val paymentKey: String,
    val orderId: String,
    val method: String,
    val customerKey: String,
    val status: PaymentStatus,
    val amount: BigDecimal,
    val currency: String,
    val createdAt: LocalDateTime,
) {

    companion object {
        fun create(
            mid: String,
            paymentKey: String,
            orderId: String,
            method: String = "CARD",
            customerKey: String,
            status: PaymentStatus,
            amount: BigDecimal,
            currency: String = "KRW",
        ): Transaction {
            return Transaction(
                id = null,
                mid = mid,
                paymentKey = paymentKey,
                orderId = orderId,
                method = method,
                customerKey = customerKey,
                status = status,
                amount = amount,
                currency = currency,
                createdAt = LocalDateTime.now()
            )
        }
    }
}