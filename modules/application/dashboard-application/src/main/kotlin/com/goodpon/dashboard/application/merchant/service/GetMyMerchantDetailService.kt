package com.goodpon.dashboard.application.merchant.service

import com.goodpon.dashboard.application.merchant.port.`in`.GetMyMerchantDetailUseCase
import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantDetail
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyMerchantDetailService(
    private val merchantRepository: MerchantRepository,
) : GetMyMerchantDetailUseCase {

    @Transactional(readOnly = true)
    override fun getMyMerchantDetail(accountId: Long, merchantId: Long): MyMerchantDetail {
        return merchantRepository.findMyMerchantDetail(accountId = accountId, merchantId = merchantId)
            ?: throw MerchantNotFoundException()
    }
}