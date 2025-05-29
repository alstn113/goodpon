package io.github.alstn113.payments.api.controller.v1.payment

import io.github.alstn113.payments.api.controller.v1.payment.request.PaymentRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/payments")
class PaymentController {

    @PostMapping
    fun create(
        @RequestBody request: PaymentRequest
    ) {
        return
    }

    @PostMapping("/confirm")
    fun confirm() {
        return
    }

    @PostMapping("/{paymentKey}/cancel")
    fun cancel(
        @PathVariable paymentKey: String
    ) {
        return
    }

    @GetMapping("/{paymentKey}")
    fun getPayment(
        @PathVariable paymentKey: String
    ) {
        return
    }

    @GetMapping("/orders/{orderId}")
    fun getPaymentByOrderId(
        @PathVariable orderId: String
    ) {
        return
    }
}