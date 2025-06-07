package io.github.alstn113.goodpon.infra.persistence.transaction

import io.github.alstn113.goodpon.domain.payment.PaymentStatus
import io.github.alstn113.goodpon.domain.transaction.Transaction
import io.github.alstn113.goodpon.infra.persistence.AuditableEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "transactions")
class TransactionEntity(
    @Id
    @Tsid
    val id: String? = null,

    @Column(nullable = false)
    val mid: String,

    @Column(nullable = false)
    val paymentKey: String,

    @Column(nullable = false)
    val orderId: String,

    @Column(nullable = false)
    val method: String,

    @Column(nullable = false)
    val customerKey: String,

    @Column(nullable = false, columnDefinition = "VARCHAR(25)")
    @Enumerated(EnumType.STRING)
    val status: PaymentStatus,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(nullable = false)
    val currency: String,
) : AuditableEntity() {

    fun toTransaction(): Transaction {
        return Transaction(
            id = id,
            mid = mid,
            paymentKey = paymentKey,
            orderId = orderId,
            method = method,
            customerKey = customerKey,
            status = status,
            amount = amount,
            currency = currency,
            createdAt = createdAt
        )
    }
}