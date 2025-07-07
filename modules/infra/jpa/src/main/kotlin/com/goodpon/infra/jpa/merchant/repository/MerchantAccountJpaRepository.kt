package com.goodpon.infra.jpa.merchant.repository

import com.goodpon.infra.jpa.merchant.entity.MerchantAccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantAccountJpaRepository : JpaRepository<MerchantAccountEntity, Long> {

    fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccountEntity?
}