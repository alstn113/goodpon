package com.goodpon.dashboard.application.merchant.port.`in`

import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantSummaries

interface GetMyMerchantsUseCase {

    fun getMyMerchants(accountId: Long): MyMerchantSummaries
}
