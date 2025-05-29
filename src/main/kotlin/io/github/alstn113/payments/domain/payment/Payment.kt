package io.github.alstn113.payments.domain.payment

import io.github.alstn113.payments.domain.Timestamps
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "payments")
class Payment(

    @Column(nullable = false)
    val orderId: String,

    @Column(nullable = false)
    val orderName: String,

    @Column(nullable = false)
    val mId: String,

    @Column
    val currency: String = "KRW",

    @Column(nullable = false)
    val method: String = "CARD",

    @Column(nullable = false)
    val totalAmount: BigDecimal,

    @Column(nullable = false, columnDefinition = "VARCHAR(25)")
    @Enumerated(EnumType.STRING)
    val status: PaymentStatus,

    @Column(nullable = false)
    val requestedAt: Instant,

    @Column
    val approvedAt: Instant? = null,

    @Embedded
    val timestamps: Timestamps = Timestamps()
) {

    @Id
    @Tsid
    val id: String? = null
}