package com.goodpon.dashboard.application.merchant.service

import com.goodpon.dashboard.application.merchant.port.`in`.GetMyMerchantsUseCase
import com.goodpon.dashboard.application.merchant.port.out.dto.MyMerchantSummary
import com.goodpon.dashboard.application.merchant.port.out.MerchantQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyMerchantsService(
    private val merchantQueryRepository: MerchantQueryRepository,
) : GetMyMerchantsUseCase {

    @Transactional(readOnly = true)
    override fun getMyMerchants(accountId: Long): List<MyMerchantSummary> {
        return merchantQueryRepository.findMyMerchants(accountId)
    }
}