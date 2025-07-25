package com.goodpon.application.dashboard.account

import com.goodpon.application.dashboard.account.port.`in`.dto.AccountInfo
import com.goodpon.application.dashboard.account.service.GetAccountInfoService
import com.goodpon.application.dashboard.account.service.accessor.AccountAccessor
import com.goodpon.domain.account.Account
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class GetAccountInfoService : DescribeSpec({

    val accountAccessor = mockk<AccountAccessor>()
    val getAccountInfoService = GetAccountInfoService(accountAccessor)

    beforeEach { clearAllMocks() }

    it("계정 식별자로 계정 정보를 조회할 수 있다.") {
        val accountId = 1L
        val account = Account.create(
            email = "test@email.com",
            password = "password",
            name = "테스터"
        ).copy(id = accountId, verified = true)
        every { accountAccessor.readById(accountId) } returns account

        val result = getAccountInfoService(accountId)

        result shouldBe AccountInfo(
            id = account.id,
            email = account.email.value,
            name = account.name.value,
            verified = account.verified
        )
        verify { accountAccessor.readById(accountId) }
    }
})