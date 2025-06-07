package io.github.alstn113.goodpon.domain.payment

import java.math.BigDecimal
import java.time.LocalDateTime

data class Payment(
    val id: String?,
    val orderName: String,
    val mId: String,
    val currency: String,
    val method: String,
    val totalAmount: BigDecimal,
    val status: PaymentStatus,
    val requestedAt: LocalDateTime = LocalDateTime.now(),
    val approvedAt: LocalDateTime?,
) {

    companion object {
        fun create(
            orderName: String,
            mId: String,
            currency: String = "KRW",
            method: String = "CARD",
            totalAmount: BigDecimal,
        ): Payment {
            return Payment(
                id = null,
                orderName = orderName,
                mId = mId,
                currency = currency,
                method = method,
                totalAmount = totalAmount,
                status = PaymentStatus.READY,
                requestedAt = LocalDateTime.now(),
                approvedAt = null
            )
        }
    }
}
