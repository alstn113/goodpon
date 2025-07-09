package com.goodpon.infra.jpa.adapter.dashboard

import com.goodpon.dashboard.application.merchant.port.out.MerchantAccountRepository
import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.infra.jpa.core.MerchantAccountCoreRepository
import org.springframework.stereotype.Repository

@Repository
class MerchantAccountJpaAdapter(
    private val merchantAccountCoreRepository: MerchantAccountCoreRepository,
) : MerchantAccountRepository {

    override fun save(merchantAccount: MerchantAccount): MerchantAccount {
        return merchantAccountCoreRepository.save(merchantAccount)
    }

    override fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccount? {
        return merchantAccountCoreRepository.findByMerchantIdAndAccountId(merchantId, accountId)
    }
}
