package io.github.alstn113.payments.application.payment

import io.github.alstn113.payments.api.controller.v1.payment.request.AuthenticatePaymentWebRequest
import io.github.alstn113.payments.api.controller.v1.payment.request.ConfirmPaymentWebRequest
import io.github.alstn113.payments.api.controller.v1.payment.request.CreatePaymentWebRequest
import io.github.alstn113.payments.application.payment.request.Amount
import io.github.alstn113.payments.application.payment.request.AuthenticatePaymentRequest
import io.github.alstn113.payments.application.payment.request.ConfirmPaymentRequest
import io.github.alstn113.payments.application.payment.request.CreatePaymentRequest

object PaymentMapper {

    fun toCreatePaymentRequest(
        request: CreatePaymentWebRequest,
        mId: Long,
    ): CreatePaymentRequest {
        return CreatePaymentRequest(
            method = request.method,
            amount = Amount(
                total = request.amount.total,
                currency = request.amount.currency
            ),
            orderId = request.orderId,
            orderName = request.orderName,
            customerEmail = request.customerEmail,
            customerName = request.customerName,
            mId = mId
        )
    }

    fun toConfirmPaymentRequest(
        request: ConfirmPaymentWebRequest,
        mId: Long,
    ): ConfirmPaymentRequest {
        return ConfirmPaymentRequest(
            paymentKey = request.paymentKey,
            orderId = request.orderId,
            amount = request.amount,
            mId = mId
        )
    }

    fun toAuthenticatePaymentRequest(
        request: AuthenticatePaymentWebRequest,
        mId: Long,
    ): AuthenticatePaymentRequest {
        return AuthenticatePaymentRequest(
            cardCode = request.cardCode,
            token = request.token,
            orderId = request.orderId,
            mId = mId
        )
    }
}