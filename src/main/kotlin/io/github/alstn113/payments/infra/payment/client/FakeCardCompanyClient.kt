package io.github.alstn113.payments.infra.payment.client

import io.github.alstn113.payments.application.payment.CardCompanyClient

class FakeCardCompanyClient : CardCompanyClient {

    override fun requestAuthentication() {
        TODO("Not yet implemented")
    }

    override fun requestApproval() {
        TODO("Not yet implemented")
    }
}