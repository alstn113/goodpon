package io.github.alstn113.payments.domain.merchant

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantRepository : JpaRepository<Merchant, String>