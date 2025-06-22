package com.goodpon.infra.db.merchant

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantAccountJpaRepository : JpaRepository<MerchantAccountEntity, Long>