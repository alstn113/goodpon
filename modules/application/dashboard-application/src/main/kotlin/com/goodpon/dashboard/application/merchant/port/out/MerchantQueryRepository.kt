package com.goodpon.dashboard.application.merchant.port.out

import com.goodpon.dashboard.application.merchant.port.out.dto.MyMerchantDetail
import com.goodpon.dashboard.application.merchant.port.out.dto.MyMerchantSummary

interface MerchantQueryRepository {

    fun findMyMerchants(accountId: Long): List<MyMerchantSummary>

    fun findMyMerchantDetail(accountId: Long, merchantId: Long): MyMerchantDetail?
}