package com.goodpon.api.dashboard.application.merchant

import com.goodpon.api.dashboard.support.AbstractIntegrationTest
import com.goodpon.application.dashboard.account.port.out.AccountRepository
import com.goodpon.application.dashboard.merchant.port.out.MerchantRepository
import com.goodpon.application.dashboard.merchant.service.GetMyMerchantDetailService
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import com.goodpon.domain.merchant.MerchantAccountRole
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class GetMyMerchantDetailServiceIT(
    private val getMyMerchantDetailService: GetMyMerchantDetailService,
    private val merchantRepository: MerchantRepository,
    private val accountRepository: AccountRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `상점 상제 정보를 조회할 수 있다`() {
        // given
        val account = Account.create(
            email = "test@goodpon.site",
            password = "password",
            name = "테스트 계정"
        )
        val savedAccount = accountRepository.save(account)

        val merchant = Merchant.create(name = "테스트 상점", accountId = savedAccount.id)
        val issuedMerchant = merchant.issueNewSecret()
        val savedMerchant = merchantRepository.save(issuedMerchant)

        // when
        val detail = getMyMerchantDetailService(accountId = savedAccount.id, merchantId = savedMerchant.id)

        // then
        detail.name shouldBe "테스트 상점"
        detail.clientId shouldBe issuedMerchant.clientId
        detail.clientSecrets.size shouldBe 2

        val detailAccount = detail.merchantAccounts.first()
        detailAccount.accountId shouldBe savedAccount.id
        detailAccount.role shouldBe MerchantAccountRole.OWNER
        detailAccount.name shouldBe savedAccount.name.value
        detailAccount.email shouldBe savedAccount.email.value
    }
}
