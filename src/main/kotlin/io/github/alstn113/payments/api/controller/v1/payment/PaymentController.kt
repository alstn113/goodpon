package io.github.alstn113.payments.api.controller.v1.payment

import io.github.alstn113.payments.api.argumentresolver.ApiKey
import io.github.alstn113.payments.api.argumentresolver.ApiKeyType
import io.github.alstn113.payments.api.controller.v1.payment.request.AuthenticatePaymentRequest
import io.github.alstn113.payments.api.controller.v1.payment.request.ConfirmPaymentRequest
import io.github.alstn113.payments.api.controller.v1.payment.request.CreatePaymentRequest
import io.github.alstn113.payments.application.payment.PaymentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/payments")
class PaymentController(
    private val paymentService: PaymentService
) {

    @PostMapping
    fun createPayment(
        @RequestBody request: CreatePaymentRequest,
        @ApiKey(ApiKeyType.CLIENT_KEY) merchantId: Long
    ) {
        return
    }

    @PostMapping("/authenticate")
    fun authenticatePayment(
        @RequestBody request: AuthenticatePaymentRequest,
        @ApiKey(ApiKeyType.CLIENT_KEY) merchantId: Long
    ) {
        return
    }

    @PostMapping("/confirm")
    fun confirmPayment(
        @RequestBody request: ConfirmPaymentRequest,
        @ApiKey(ApiKeyType.SECRET_KEY) merchantId: Long
    ) {
        return
    }

    @PostMapping("/{paymentKey}/cancel")
    fun cancelPayment(
        @PathVariable paymentKey: String
    ) {
        // TODO: Implement the logic to confirm the payment
        return
    }

    @GetMapping("/{paymentKey}")
    fun getPaymentByPaymentKey(
        @PathVariable paymentKey: String
    ) {
        // TODO: Implement the logic to confirm the payment
        return
    }

    @GetMapping("/orders/{orderId}")
    fun getPaymentByOrderId(
        @PathVariable orderId: String
    ) {
        // TODO: Implement the logic to confirm the payment
        return
    }
}