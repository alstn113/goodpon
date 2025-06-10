package com.goodpon.common.domain.merchant

interface MerchantRepository {

    fun findByClientKey(clientKey: String): Merchant?
    fun findBySecretKey(secretKey: String): Merchant?
}
