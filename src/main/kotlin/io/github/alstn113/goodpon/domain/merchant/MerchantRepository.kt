package io.github.alstn113.goodpon.domain.merchant

interface MerchantRepository {

    fun findByClientKey(clientKey: String): Merchant?
    fun findBySecretKey(secretKey: String): Merchant?
}
