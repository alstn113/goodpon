package com.goodpon.core.domain.merchant

import com.goodpon.core.domain.account.Account
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MerchantAppender(
    private val merchantRepository: MerchantRepository,
    private val merchantAccountRepository: MerchantAccountRepository,
    private val secretKeyGenerator: SecretKeyGenerator,
) {

    @Transactional
    fun append(merchantName: String, account: Account): Merchant {
        val merchant = Merchant(
            name = merchantName,
            secretKey = secretKeyGenerator.generate(),
        )
        val savedMerchant = merchantRepository.save(merchant)

        val merchantOwner = MerchantAccount(
            merchantId = savedMerchant.id,
            accountId = account.id,
            role = MerchantAccountRole.OWNER
        )
        merchantAccountRepository.save(merchantOwner)

        return savedMerchant
    }
}
