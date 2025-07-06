package com.goodpon.partner.application.merchant.port.out

import com.goodpon.domain.merchant.Merchant

interface MerchantRepository {

    fun findBySecretKey(secretKey: String): Merchant?
}