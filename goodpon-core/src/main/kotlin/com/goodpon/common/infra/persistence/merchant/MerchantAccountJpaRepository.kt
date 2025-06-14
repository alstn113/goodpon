package com.goodpon.common.infra.persistence.merchant

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantAccountJpaRepository : JpaRepository<MerchantAccountEntity, Long>