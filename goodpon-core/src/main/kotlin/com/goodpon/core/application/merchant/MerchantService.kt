package com.goodpon.core.application.merchant

import com.goodpon.core.application.merchant.request.CreateMerchantRequest
import com.goodpon.core.application.merchant.response.CreateMerchantResponse
import com.goodpon.core.application.merchant.response.MerchantInfo
import com.goodpon.core.domain.account.AccountReader
import com.goodpon.core.domain.merchant.MerchantAppender
import com.goodpon.core.domain.merchant.MerchantReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MerchantService(
    private val merchantReader: MerchantReader,
    private val accountReader: AccountReader,
    private val merchantAppender: MerchantAppender,
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
        val account = accountReader.readById(request.accountPrincipal.accountId)
        if (account.isNotVerified()) {
            throw IllegalArgumentException("Account is not verified")
        }

        val merchant = merchantAppender.append(
            merchantName = request.name,
            account = account
        )

        return CreateMerchantResponse(
            id = merchant.id,
            name = merchant.name,
        )
    }
}
