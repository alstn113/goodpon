package io.github.alstn113.payments.application.payment

import io.github.alstn113.payments.domain.payment.PaymentRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {

    fun createPayment() {}

    fun authenticatePayment() {}

    fun confirmPayment() {}
}