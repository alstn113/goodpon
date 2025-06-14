package com.goodpon.infra.db.merchant

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantJpaRepository : JpaRepository<MerchantEntity, Long> {

    fun findBySecretKey(secretKey: String): MerchantEntity?
}
