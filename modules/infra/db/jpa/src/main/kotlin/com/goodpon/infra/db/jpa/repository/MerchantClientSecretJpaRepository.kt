package com.goodpon.infra.db.jpa.repository

import com.goodpon.infra.db.jpa.entity.MerchantClientSecretEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantClientSecretJpaRepository : JpaRepository<MerchantClientSecretEntity, Long> {

    fun findByMerchantId(merchantId: Long): List<MerchantClientSecretEntity>
}