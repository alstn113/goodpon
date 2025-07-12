package com.goodpon.dashboard.application.merchant

import com.goodpon.dashboard.application.account.service.accessor.AccountAccessor
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantCommand
import com.goodpon.dashboard.application.merchant.service.CreateMerchantService
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CreateMerchantServiceTest : DescribeSpec({

    val accountAccessor = mockk<AccountAccessor>()
    val merchantAccessor = mockk<MerchantAccessor>()
    val createMerchantService = CreateMerchantService(accountAccessor, merchantAccessor)

    describe("createMerchant") {
        it("상점을 생성할 수 있다.") {
            val command = CreateMerchantCommand(accountId = 1L, name = "Test Merchant")
            val account = Account.create(
                email = "test@goodpon.site",
                password = "hashedPassword",
                name = "Test User"
            )
            val merchant = Merchant.create(name = command.name, accountId = command.accountId)

            every { accountAccessor.readById(command.accountId) } returns account
            every { merchantAccessor.createMerchant(command.name, account) } returns merchant

            val result = createMerchantService.createMerchant(command)

            result.name shouldBe "Test Merchant"
            result.clientId shouldBe merchant.clientId
            result.merchantAccounts.size shouldBe 1
            result.clientSecrets.size shouldBe 1

            verify { accountAccessor.readById(command.accountId) }
            verify { merchantAccessor.createMerchant(command.name, account) }
        }
    }
})