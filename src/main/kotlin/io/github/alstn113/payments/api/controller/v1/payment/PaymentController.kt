package io.github.alstn113.payments.api.controller.v1.payment

import io.github.alstn113.payments.api.argumentresolver.ApiKey
import io.github.alstn113.payments.api.argumentresolver.ApiKeyType
import io.github.alstn113.payments.api.controller.v1.payment.request.AuthenticatePaymentWebRequest
import io.github.alstn113.payments.api.controller.v1.payment.request.ConfirmPaymentWebRequest
import io.github.alstn113.payments.api.controller.v1.payment.request.CreatePaymentWebRequest
import io.github.alstn113.payments.application.payment.PaymentMapper
import io.github.alstn113.payments.application.payment.PaymentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/payments")
class PaymentController(
    private val paymentService: PaymentService,
) {

    @PostMapping
    fun createPayment(
        @RequestBody request: CreatePaymentWebRequest,
        @ApiKey(ApiKeyType.CLIENT_KEY) mId: Long,
    ) {
        val appRequest = PaymentMapper.toCreatePaymentRequest(request, mId)
        return paymentService.createPayment(appRequest)
    }

    @PostMapping("/authenticate")
    fun authenticatePayment(
        @RequestBody request: AuthenticatePaymentWebRequest,
        @ApiKey(ApiKeyType.CLIENT_KEY) merchantId: Long,
    ) {
        val appRequest = PaymentMapper.toAuthenticatePaymentRequest(request, merchantId)
        return paymentService.authenticatePayment(appRequest)
    }

    @PostMapping("/confirm")
    fun confirmPayment(
        @RequestBody request: ConfirmPaymentWebRequest,
        @ApiKey(ApiKeyType.SECRET_KEY) merchantId: Long,
    ) {
        val appRequest = PaymentMapper.toConfirmPaymentRequest(request, merchantId)
        return paymentService.confirmPayment(appRequest)
    }

    @PostMapping("/{paymentKey}/cancel")
    fun cancelPayment(
        @PathVariable paymentKey: String,
    ) {
        // TODO: Implement the logic to confirm the payment
        return
    }

    @GetMapping("/{paymentKey}")
    fun getPaymentByPaymentKey(
        @PathVariable paymentKey: String,
    ) {
        // TODO: Implement the logic to confirm the payment
        return
    }

    @GetMapping("/orders/{orderId}")
    fun getPaymentByOrderId(
        @PathVariable orderId: String,
    ) {
        // TODO: Implement the logic to confirm the payment
        return
    }
}