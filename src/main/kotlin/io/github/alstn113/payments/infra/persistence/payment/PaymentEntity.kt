package io.github.alstn113.payments.infra.persistence.payment

import io.github.alstn113.payments.domain.payment.Payment
import io.github.alstn113.payments.domain.payment.PaymentStatus
import io.github.alstn113.payments.infra.persistence.AuditableEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "payments")
class PaymentEntity(

    @Column(nullable = false)
    val orderId: String,

    @Column(nullable = false)
    val orderName: String,

    @Column(nullable = false)
    val mId: String,

    @Column
    val currency: String,

    @Column(nullable = false)
    val method: String,

    @Column(nullable = false)
    val totalAmount: BigDecimal,

    @Column(nullable = false, columnDefinition = "VARCHAR(25)")
    @Enumerated(EnumType.STRING)
    val status: PaymentStatus,

    @Column
    val approvedAt: LocalDateTime? = null,
) : AuditableEntity() {

    @Id
    @Tsid
    val id: String? = null

    fun toPayment(): Payment {
        return Payment(
            id = id!!,
            orderName = orderName,
            mId = mId,
            currency = currency,
            method = method,
            totalAmount = totalAmount,
            status = status,
            requestedAt = createdAt,
            approvedAt = approvedAt,
        )
    }
}