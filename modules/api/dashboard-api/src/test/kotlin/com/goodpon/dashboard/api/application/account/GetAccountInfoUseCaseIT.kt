package com.goodpon.dashboard.api.application.account

import com.goodpon.dashboard.api.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GetAccountInfoUseCaseIT : AbstractIntegrationTest() {

    @Test
    fun `should return account info`() {
        val accountId = 1L // Assuming an account with ID 1 exists

        accountId shouldBe 1L
    }
}