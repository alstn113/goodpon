package com.goodpon.infra.jpa.repository

import com.goodpon.infra.jpa.entity.MerchantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantJpaRepository : JpaRepository<MerchantEntity, Long> {

    fun findBySecretKey(secretKey: String): MerchantEntity?
}
