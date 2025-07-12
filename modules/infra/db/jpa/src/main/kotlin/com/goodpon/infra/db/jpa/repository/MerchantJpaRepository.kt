package com.goodpon.infra.db.jpa.repository

import com.goodpon.infra.db.jpa.entity.MerchantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantJpaRepository : JpaRepository<MerchantEntity, Long> {

    fun findByClientId(clientId: String): MerchantEntity?
}
