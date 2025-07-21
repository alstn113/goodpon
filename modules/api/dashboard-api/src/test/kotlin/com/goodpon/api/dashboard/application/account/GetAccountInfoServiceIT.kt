package com.goodpon.api.dashboard.application.account

import com.goodpon.api.dashboard.support.AbstractIntegrationTest
import com.goodpon.application.dashboard.account.port.out.AccountRepository
import com.goodpon.application.dashboard.account.service.GetAccountInfoService
import com.goodpon.domain.account.Account
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GetAccountInfoServiceIT(
    private val getAccountInfoService: GetAccountInfoService,
    private val accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `계정 정보를 조회할 수 있다`() {
        // given
        val email = "test@goodpon.site"
        val account = Account.create(
            email = email,
            password = "password",
            name = "테스트 계정"
        )
        val savedAccount = accountRepository.save(account)

        // when
        val accountInfo = getAccountInfoService(savedAccount.id)

        // then
        val foundAccount = accountRepository.findById(savedAccount.id)
        foundAccount.shouldNotBeNull()
        accountInfo.email shouldBe email
        foundAccount.email.value shouldBe email
    }
}