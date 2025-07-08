package com.goodpon.infra.jpa.account

import com.goodpon.infra.jpa.IntegrationTest
import com.goodpon.infra.jpa.account.repository.AccountJpaRepository
import com.goodpon.infra.jpa.merchant.adapter.MerchantAccountJpaAdapter

class AccountJpaAdapterIT(
    private val accountJpaAdapter: MerchantAccountJpaAdapter,
    private val accountJpaRepository: AccountJpaRepository,
) : IntegrationTest()