package io.github.alstn113.goodpon.application.payment

import io.github.alstn113.goodpon.application.payment.request.AuthenticatePaymentRequest
import io.github.alstn113.goodpon.application.payment.request.ConfirmPaymentRequest
import io.github.alstn113.goodpon.application.payment.request.CreatePaymentRequest
import io.github.alstn113.goodpon.domain.payment.PaymentRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
) {

    fun createPayment(request: CreatePaymentRequest) {}

    fun authenticatePayment(request: AuthenticatePaymentRequest) {}

    fun confirmPayment(request: ConfirmPaymentRequest) {

    }
}