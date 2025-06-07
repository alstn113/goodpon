package io.github.alstn113.goodpon.application.payment

interface CardCompanyClient {

    fun requestAuthentication()

    fun requestConfirmation()
}