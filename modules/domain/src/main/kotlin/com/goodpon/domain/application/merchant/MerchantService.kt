package com.goodpon.domain.application.merchant

import com.goodpon.domain.application.account.accessor.AccountReader
import com.goodpon.domain.application.merchant.accessor.MerchantReader
import com.goodpon.domain.application.merchant.accessor.MerchantStore
import com.goodpon.domain.application.merchant.request.CreateMerchantRequest
import com.goodpon.domain.application.merchant.response.CreateMerchantResponse
import com.goodpon.domain.application.merchant.response.MerchantAccountInfo
import com.goodpon.domain.application.merchant.response.MerchantInfo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantService(
    private val merchantReader: MerchantReader,
    private val accountReader: AccountReader,
    private val merchantStore: MerchantStore,
) {

    @Transactional(readOnly = true)
    fun getMerchantInfoBySecretKey(secretKey: String): MerchantInfo {
        val merchant = merchantReader.readBySecretKey(secretKey)

        return MerchantInfo(
            id = merchant.id,
            name = merchant.name,
            secretKey = merchant.secretKey,
        )
    }

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
