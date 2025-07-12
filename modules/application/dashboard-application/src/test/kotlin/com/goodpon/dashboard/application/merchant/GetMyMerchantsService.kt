package com.goodpon.dashboard.application.merchant

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.service.GetMyMerchantsService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class GetMyMerchantsService : DescribeSpec({

    val merchantRepository = mockk<MerchantRepository>()
    val getMyMerchantsService = GetMyMerchantsService(merchantRepository)

    describe("getMyMerchants") {
        it("내 상점 목록을 조회할 수 있다.") {
            val accountId = 1L

            every { merchantRepository.findMyMerchants(accountId) } returns listOf()

            getMyMerchantsService.getMyMerchants(accountId)

            verify { merchantRepository.findMyMerchants(accountId) }
        }
    }
})