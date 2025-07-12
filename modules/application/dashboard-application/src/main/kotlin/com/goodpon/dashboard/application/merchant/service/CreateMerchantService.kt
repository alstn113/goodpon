package com.goodpon.dashboard.application.merchant.service

import com.goodpon.dashboard.application.account.service.accessor.AccountReader
import com.goodpon.dashboard.application.merchant.port.`in`.CreateMerchantUseCase
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantCommand
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantResult
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantStore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateMerchantService(
    private val accountReader: AccountReader,
    private val merchantStore: MerchantStore,
) : CreateMerchantUseCase {

    @Transactional
    override fun createMerchant(command: CreateMerchantCommand): CreateMerchantResult {
        val account = accountReader.readById(command.accountId)
        val merchant = merchantStore.createMerchant(command.name, account)

        return CreateMerchantResult.from(merchant)
    }
}
