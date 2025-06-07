package io.github.alstn113.goodpon.infra.persistence.payment

import io.github.alstn113.goodpon.domain.payment.PaymentRepository
import org.springframework.stereotype.Repository

@Repository
class PaymentCoreRepository(
    private val paymentJpaRepository: PaymentJpaRepository,
) : PaymentRepository
