package com.goodpon.dashboard.application.merchant.port.out

import com.goodpon.domain.merchant.Merchant

interface MerchantRepository {

    fun save(merchant: Merchant): Merchant

    fun findById(merchantId: Long): Merchant?
}