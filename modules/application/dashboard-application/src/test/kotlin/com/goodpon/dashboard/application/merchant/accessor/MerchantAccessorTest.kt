package com.goodpon.dashboard.application.merchant.accessor

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import com.goodpon.domain.account.Account
import com.goodpon.domain.merchant.Merchant
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class MerchantAccessorTest : DescribeSpec({

    val merchantRepository = mockk<MerchantRepository>()
    val merchantAccessor = MerchantAccessor(merchantRepository)

    beforeEach {
        clearAllMocks()
    }

    describe("readById") {
        it("존재하지 않는 상점일 경우 예외를 발생시킨다.") {
            val merchantId = 1L

            every { merchantRepository.findById(merchantId) } returns null

            shouldThrow<MerchantNotFoundException> {
                merchantAccessor.readById(merchantId)
            }
        }
    }

    describe("createMerchant") {
        it("상점을 생성하고 저장한다.") {
            val account = Account.create(
                email = "test@goodpon.site",
                password = "password",
                name = "Test User"
            )
            val merchant = Merchant.create("Test Merchant", account.id)

            every { merchantRepository.save(any()) } returns merchant

            val result = merchantAccessor.createMerchant("Test Merchant", account)

            result shouldBe merchant
            verify { merchantRepository.save(any()) }
        }
    }
})
