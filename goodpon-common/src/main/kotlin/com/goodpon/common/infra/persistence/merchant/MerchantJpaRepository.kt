package com.goodpon.common.infra.persistence.merchant

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantJpaRepository : JpaRepository<MerchantEntity, String> {

    fun findBySecretKey(secretKey: String): MerchantEntity?
}
