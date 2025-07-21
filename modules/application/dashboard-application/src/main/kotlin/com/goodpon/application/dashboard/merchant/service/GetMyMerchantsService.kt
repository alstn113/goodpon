package com.goodpon.application.dashboard.merchant.service

import com.goodpon.application.dashboard.merchant.port.`in`.GetMyMerchantsUseCase
import com.goodpon.application.dashboard.merchant.port.out.MerchantRepository
import com.goodpon.application.dashboard.merchant.service.dto.MyMerchantSummaries
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyMerchantsService(
    private val merchantRepository: MerchantRepository,
) : GetMyMerchantsUseCase {

    @Transactional(readOnly = true)
    override fun invoke(accountId: Long): MyMerchantSummaries {
        val merchants = merchantRepository.findMyMerchants(accountId)

        return MyMerchantSummaries(merchants = merchants)
    }
}