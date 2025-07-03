package com.goodpon.infra.jpa.merchant

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantJpaRepository : JpaRepository<MerchantEntity, Long> {
    fun findBySecretKey(secretKey: String): MerchantEntity?
}
