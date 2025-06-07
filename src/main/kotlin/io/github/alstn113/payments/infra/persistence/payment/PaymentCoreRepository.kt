package io.github.alstn113.payments.infra.persistence.payment

import io.github.alstn113.payments.domain.payment.PaymentRepository
import org.springframework.stereotype.Repository

@Repository
class PaymentCoreRepository(
    private val paymentJpaRepository: PaymentJpaRepository,
) : PaymentRepository
