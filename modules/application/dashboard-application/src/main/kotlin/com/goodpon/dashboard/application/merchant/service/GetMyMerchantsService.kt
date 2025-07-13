package com.goodpon.dashboard.application.merchant.service

import com.goodpon.dashboard.application.merchant.port.`in`.GetMyMerchantsUseCase
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantSummary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyMerchantsService(
    private val merchantRepository: MerchantRepository,
) : GetMyMerchantsUseCase {

    @Transactional(readOnly = true)
    override fun getMyMerchants(accountId: Long): List<MyMerchantSummary> {
        return merchantRepository.findMyMerchants(accountId)
    }
}