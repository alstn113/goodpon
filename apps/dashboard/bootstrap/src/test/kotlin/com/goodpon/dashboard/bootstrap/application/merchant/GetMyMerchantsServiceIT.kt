package com.goodpon.dashboard.bootstrap.application.merchant

import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.service.GetMyMerchantsService
import com.goodpon.dashboard.bootstrap.support.AbstractIntegrationTest
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GetMyMerchantsServiceIT(
    private val getMyMerchantsService: GetMyMerchantsService,
    private val merchantRepository: MerchantRepository,
    private val accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `내 상점 목록을 조회할 수 있다`() {
        // given
        val account = Account.create(
            email = "test@goodpon.site",
            password = "password",
            name = "테스트 계정"
        )
        val savedAccount = accountRepository.save(account)

        val firstMerchant = Merchant.create(name = "첫번째 상점", accountId = savedAccount.id)
        merchantRepository.save(firstMerchant)

        val secondMerchant = Merchant.create(name = "두번째 상점", accountId = savedAccount.id)
        merchantRepository.save(secondMerchant)

        // when
        val result = getMyMerchantsService(savedAccount.id)

        // then
        result.merchants.size shouldBe 2
        result.merchants.map { it.name } shouldBe listOf("두번째 상점", "첫번째 상점")
    }
}