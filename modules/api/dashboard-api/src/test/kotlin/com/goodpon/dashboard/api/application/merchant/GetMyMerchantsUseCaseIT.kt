package com.goodpon.dashboard.api.application.merchant

import com.goodpon.dashboard.api.support.AbstractIntegrationTest
import com.goodpon.dashboard.application.account.port.out.AccountRepository
import com.goodpon.dashboard.application.merchant.port.`in`.GetMyMerchantsUseCase
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GetMyMerchantsUseCaseIT(
    private val getMyMerchantsUseCase: GetMyMerchantsUseCase,
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
        val merchants = getMyMerchantsUseCase.getMyMerchants(savedAccount.id)

        // then
        merchants.size shouldBe 2
        merchants.map { it.name } shouldBe listOf("두번째 상점", "첫번째 상점")
    }
}