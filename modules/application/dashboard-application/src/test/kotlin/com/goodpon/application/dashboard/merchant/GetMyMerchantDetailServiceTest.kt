package com.goodpon.application.dashboard.merchant

import com.goodpon.application.dashboard.merchant.port.out.MerchantRepository
import com.goodpon.application.dashboard.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.application.dashboard.merchant.service.GetMyMerchantDetailService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class GetMyMerchantDetailServiceTest : DescribeSpec({

    val merchantRepository = mockk<MerchantRepository>()
    val getMyMerchantDetailService = GetMyMerchantDetailService(merchantRepository)

    describe("getMyMerchantDetail") {
        it("계정 식별자와 상점 식별자로 상점을 찾지 못했을 경우 예외를 발생시킨다.") {
            val accountId = 1L
            val merchantId = 1L

            every { merchantRepository.findMyMerchantDetail(accountId, merchantId) } returns null

            shouldThrow<MerchantNotFoundException> {
                getMyMerchantDetailService(accountId, merchantId)
            }
        }
    }
})