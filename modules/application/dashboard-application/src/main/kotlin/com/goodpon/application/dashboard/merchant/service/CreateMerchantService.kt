package com.goodpon.application.dashboard.merchant.service

import com.goodpon.application.dashboard.account.service.accessor.AccountAccessor
import com.goodpon.application.dashboard.merchant.port.`in`.CreateMerchantUseCase
import com.goodpon.application.dashboard.merchant.port.`in`.dto.CreateMerchantCommand
import com.goodpon.application.dashboard.merchant.port.`in`.dto.CreateMerchantResult
import com.goodpon.application.dashboard.merchant.service.accessor.MerchantAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateMerchantService(
    private val accountAccessor: AccountAccessor,
    private val merchantAccessor: MerchantAccessor,
) : CreateMerchantUseCase {

    @Transactional
    override fun invoke(command: CreateMerchantCommand): CreateMerchantResult {
        val account = accountAccessor.readById(command.accountId)
        val merchant = merchantAccessor.createMerchant(command.name, account)

        return CreateMerchantResult.from(merchant)
    }
}
