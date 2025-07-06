package com.goodpon.dashboard.application.merchant

import com.goodpon.dashboard.application.account.accessor.AccountReader
import com.goodpon.dashboard.application.merchant.accessor.MerchantStore
import com.goodpon.dashboard.application.merchant.request.CreateMerchantRequest
import com.goodpon.dashboard.application.merchant.response.CreateMerchantResponse
import com.goodpon.dashboard.application.merchant.response.MerchantAccountInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantService(
    private val accountReader: AccountReader,
    private val merchantStore: MerchantStore,
) {

    @Transactional
    fun createMerchant(request: CreateMerchantRequest): CreateMerchantResponse {
        val account = accountReader.readById(request.accountId)
        val (merchant, merchantAccount) = merchantStore.createMerchant(merchantName = request.name, account = account)

        val merchantOwnerInfo = MerchantAccountInfo(
            id = merchantAccount.id,
            accountId = account.id,
            role = merchantAccount.role,
        )
        return CreateMerchantResponse(
            id = merchant.id,
            name = merchant.name,
            secretKey = merchant.secretKey,
            accounts = listOf(merchantOwnerInfo)
        )
    }
}
