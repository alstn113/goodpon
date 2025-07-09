package com.goodpon.infra.jpa.adapter.dashboard

import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.jpa.AbstractIntegrationTest
import com.goodpon.infra.jpa.repository.MerchantJpaRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MerchantJpaAdapterIT(
    private val merchantJpaAdapter: MerchantJpaAdapter,
    private val merchantJpaRepository: MerchantJpaRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `save1`() {
        val merchant = Merchant.create(name = "name")

        merchantJpaAdapter.save(merchant)

        merchantJpaRepository.findAll().size shouldBe 1
    }

    @Test
    fun `save2`() {
        val merchant = Merchant.create(name = "name")

        merchantJpaAdapter.save(merchant)

        merchantJpaRepository.findAll().size shouldBe 1
    }
}