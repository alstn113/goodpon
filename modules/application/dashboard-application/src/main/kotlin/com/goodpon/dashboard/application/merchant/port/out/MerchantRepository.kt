package com.goodpon.dashboard.application.merchant.port.out

import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantDetail
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantSummary
import com.goodpon.domain.merchant.Merchant

interface MerchantRepository {

    fun save(merchant: Merchant): Merchant

    fun findById(id: Long): Merchant?

    fun findMyMerchants(accountId: Long): List<MyMerchantSummary>

    fun findMyMerchantDetail(accountId: Long, merchantId: Long): MyMerchantDetail?
}