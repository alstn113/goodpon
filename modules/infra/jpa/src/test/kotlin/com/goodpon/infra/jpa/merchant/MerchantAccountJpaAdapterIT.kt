package com.goodpon.infra.jpa.merchant

import com.goodpon.infra.jpa.AbstractIntegrationTest
import com.goodpon.infra.jpa.merchant.adapter.MerchantAccountJpaAdapter
import com.goodpon.infra.jpa.merchant.repository.MerchantAccountJpaRepository

class MerchantAccountJpaAdapterIT(
    private val merchantAccountJpaAdapter: MerchantAccountJpaAdapter,
    private val merchantAccountJpaRepository: MerchantAccountJpaRepository,
) : AbstractIntegrationTest()