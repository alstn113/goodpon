package io.github.alstn113.goodpon.infra.persistence.merchant

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantJpaRepository : JpaRepository<MerchantEntity, String> {

    fun findByClientKey(clientKey: String): MerchantEntity?
    fun findBySecretKey(secretKey: String): MerchantEntity?
}
