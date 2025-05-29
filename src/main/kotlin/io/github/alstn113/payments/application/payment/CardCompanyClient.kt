package io.github.alstn113.payments.application.payment

interface CardCompanyClient {

    fun requestAuthentication()

    fun requestConfirmation()
}