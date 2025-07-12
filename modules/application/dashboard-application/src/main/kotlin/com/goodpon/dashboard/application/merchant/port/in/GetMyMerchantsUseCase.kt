package com.goodpon.dashboard.application.merchant.port.`in`

import com.goodpon.dashboard.application.merchant.port.out.dto.MyMerchantSummary

interface GetMyMerchantsUseCase {

    fun getMyMerchants(accountId: Long): List<MyMerchantSummary>
}
