package com.goodpon.application.dashboard.merchant.service

import com.goodpon.application.dashboard.merchant.port.`in`.GetMyMerchantDetailUseCase
import com.goodpon.application.dashboard.merchant.port.out.MerchantRepository
import com.goodpon.application.dashboard.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.application.dashboard.merchant.service.dto.MyMerchantDetail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyMerchantDetailService(
    private val merchantRepository: MerchantRepository,
) : GetMyMerchantDetailUseCase {

    @Transactional(readOnly = true)
    override fun invoke(accountId: Long, merchantId: Long): MyMerchantDetail {
        return merchantRepository.findMyMerchantDetail(accountId = accountId, merchantId = merchantId)
            ?: throw MerchantNotFoundException()
    }
}