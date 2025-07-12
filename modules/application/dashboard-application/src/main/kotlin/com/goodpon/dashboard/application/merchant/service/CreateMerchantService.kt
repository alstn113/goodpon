package com.goodpon.dashboard.application.merchant.service

import com.goodpon.dashboard.application.account.service.accessor.AccountAccessor
import com.goodpon.dashboard.application.merchant.port.`in`.CreateMerchantUseCase
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantCommand
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantResult
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateMerchantService(
    private val accountAccessor: AccountAccessor,
    private val merchantAccessor: MerchantAccessor,
) : CreateMerchantUseCase {

    @Transactional
    override fun createMerchant(command: CreateMerchantCommand): CreateMerchantResult {
        val account = accountAccessor.readById(command.accountId)
        val merchant = merchantAccessor.createMerchant(command.name, account)

        return CreateMerchantResult.from(merchant)
    }
}
