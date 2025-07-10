package com.goodpon.dashboard.api.application.account

import com.goodpon.dashboard.api.support.AbstractIntegrationTest
import com.goodpon.dashboard.application.account.port.`in`.GetAccountInfoUseCase
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.domain.account.Account
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GetAccountInfoUseCaseIT(
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
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
        val accountInfo = getAccountInfoUseCase.getAccountInfo(savedAccount.id)

        // then
        val foundAccount = accountRepository.findById(savedAccount.id)
        foundAccount.shouldNotBeNull()
        accountInfo.email shouldBe email
        foundAccount.email.value shouldBe email
    }
}