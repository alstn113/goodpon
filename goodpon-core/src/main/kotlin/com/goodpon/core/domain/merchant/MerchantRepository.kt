package com.goodpon.core.domain.merchant

interface MerchantRepository {

    fun save(merchant: Merchant): Merchant
    fun findBySecretKey(secretKey: String): Merchant?
}
