package com.goodpon.infra.jpa.adapter.dashboard

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.jpa.AbstractIntegrationTest
import com.goodpon.infra.jpa.repository.MerchantJpaRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class MerchantRepositoryIT(
    private val merchantRepository: MerchantRepository,
    private val merchantJpaRepository: MerchantJpaRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `save1`() {
        val merchant = Merchant.create(name = "name")

        merchantRepository.save(merchant)

        merchantJpaRepository.findAll().size shouldBe 1
    }

    @Test
    fun `save2`() {
        val merchant = Merchant.create(name = "name")

        merchantRepository.save(merchant)

        merchantJpaRepository.findAll().size shouldBe 1
    }
}