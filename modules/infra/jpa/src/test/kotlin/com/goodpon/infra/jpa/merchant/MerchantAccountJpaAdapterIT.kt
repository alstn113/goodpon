package com.goodpon.infra.jpa.merchant

import com.goodpon.infra.jpa.IntegrationTest
import com.goodpon.infra.jpa.merchant.adapter.MerchantAccountJpaAdapter
import com.goodpon.infra.jpa.merchant.repository.MerchantAccountJpaRepository

class MerchantAccountJpaAdapterIT(
    private val merchantAccountJpaAdapter: MerchantAccountJpaAdapter,
    private val merchantAccountJpaRepository: MerchantAccountJpaRepository,
) : IntegrationTest()