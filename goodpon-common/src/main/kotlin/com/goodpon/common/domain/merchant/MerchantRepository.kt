package com.goodpon.common.domain.merchant

interface MerchantRepository {

    fun findBySecretKey(secretKey: String): Merchant?
}
