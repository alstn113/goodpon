package com.goodpon.dashboard.application.merchant.service

import com.goodpon.dashboard.application.merchant.port.`in`.GetMyMerchantDetailUseCase
import com.goodpon.dashboard.application.merchant.port.out.dto.MyMerchantDetail
import com.goodpon.dashboard.application.merchant.port.out.MerchantQueryRepository
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMyMerchantDetailService(
    private val merchantQueryRepository: MerchantQueryRepository,
) : GetMyMerchantDetailUseCase {

    @Transactional(readOnly = true)
    override fun getMyMerchantDetail(accountId: Long, merchantId: Long): MyMerchantDetail {
        return merchantQueryRepository.findMyMerchantDetail(accountId = accountId, merchantId = merchantId)
            ?: throw MerchantNotFoundException()
    }
}