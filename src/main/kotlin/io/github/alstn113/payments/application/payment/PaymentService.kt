package io.github.alstn113.payments.application.payment

import io.github.alstn113.payments.application.payment.request.AuthenticatePaymentRequest
import io.github.alstn113.payments.application.payment.request.ConfirmPaymentRequest
import io.github.alstn113.payments.application.payment.request.CreatePaymentRequest
import io.github.alstn113.payments.infra.persistence.payment.PaymentJpaRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentJpaRepository: PaymentJpaRepository,
) {

    fun createPayment(request: CreatePaymentRequest) {}

    fun authenticatePayment(request: AuthenticatePaymentRequest) {}

    fun confirmPayment(request: ConfirmPaymentRequest) {}
}