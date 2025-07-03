package com.goodpon.infra.jpa.merchant

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantAccountJpaRepository : JpaRepository<MerchantAccountEntity, Long> {
    fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccountEntity?
}