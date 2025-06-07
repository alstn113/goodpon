package io.github.alstn113.goodpon.infra.persistence.merchant

import io.github.alstn113.goodpon.domain.merchant.MerchantRepository
import org.springframework.stereotype.Repository

@Repository
class MerchantCoreRepository(
    private val merchantJpaRepository: MerchantJpaRepository,
) : MerchantRepository