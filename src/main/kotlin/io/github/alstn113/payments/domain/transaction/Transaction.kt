package io.github.alstn113.payments.domain.transaction

import io.github.alstn113.payments.domain.Timestamps
import io.github.alstn113.payments.domain.payment.PaymentStatus
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "transactions")
class Transaction(
    @Column(nullable = false)
    val mid: String,

    @Column(nullable = false)
    val paymentKey: String,

    @Column(nullable = false)
    val orderId: String,

    @Column(nullable = false)
    val method: String = "CARD",

    @Column(nullable = false)
    val customerKey: String,

    @Column(nullable = false, columnDefinition = "VARCHAR(25)")
    @Enumerated(EnumType.STRING)
    val status: PaymentStatus,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Column(nullable = false)
    val currency: String = "KRW",

    @Embedded
    val timestamps: Timestamps = Timestamps()
) {

    @Id
    @Tsid
    val id: String? = null
}