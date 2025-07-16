package com.goodpon.dashboard.api.application.merchant

import com.goodpon.dashboard.api.support.AbstractIntegrationTest
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantCommand
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.service.CreateMerchantService
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.MerchantAccountRole
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CreateAuthenticateMerchantServiceIT(
    private val createMerchantService: CreateMerchantService,
    private val accountRepository: AccountRepository,
    private val merchantRepository: MerchantRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `상점을 생성할 수 있다`() {
        // given
        val account = Account.create(
            email = "test@goodpon.site",
            password = "password",
            name = "테스트 계정"
        )
        val savedAccount = accountRepository.save(account)
        val command = CreateMerchantCommand(
            accountId = savedAccount.id,
            name = "테스트 상점",
        )

        // when
        val result = createMerchantService.createMerchant(command)

        // then
        result.id.shouldNotBeNull()
        result.name shouldBe command.name
        result.clientId.startsWith("ck_") shouldBe true
        result.clientSecrets.first().secret.startsWith("sk_") shouldBe true
        val owner = result.merchantAccounts.first()
        owner.accountId shouldBe savedAccount.id
        owner.role shouldBe MerchantAccountRole.OWNER

        val foundMerchant = merchantRepository.findById(result.id)
        foundMerchant.shouldNotBeNull()
        foundMerchant.id shouldBe savedAccount.id
    }
}