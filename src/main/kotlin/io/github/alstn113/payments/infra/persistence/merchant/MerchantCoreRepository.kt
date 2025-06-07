package io.github.alstn113.payments.infra.persistence.merchant

import io.github.alstn113.payments.domain.merchant.MerchantRepository
import org.springframework.stereotype.Repository

@Repository
class MerchantCoreRepository(
    private val merchantJpaRepository: MerchantJpaRepository,
) : MerchantRepository