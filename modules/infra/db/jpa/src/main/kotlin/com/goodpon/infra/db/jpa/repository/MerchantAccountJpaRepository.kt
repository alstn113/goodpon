package com.goodpon.infra.db.jpa.repository

import com.goodpon.infra.db.jpa.entity.MerchantAccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantAccountJpaRepository : JpaRepository<MerchantAccountEntity, Long> {

    fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccountEntity?

    fun findByMerchantId(merchantId: Long): List<MerchantAccountEntity>
}