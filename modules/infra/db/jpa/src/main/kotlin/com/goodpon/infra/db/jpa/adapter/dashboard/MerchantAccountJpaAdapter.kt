package com.goodpon.infra.db.jpa.adapter.dashboard

import com.goodpon.dashboard.application.merchant.port.out.MerchantAccountRepository
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantAccountNotFoundException
import com.goodpon.domain.merchant.MerchantAccount
import com.goodpon.infra.db.jpa.core.MerchantAccountCoreRepository
import org.springframework.stereotype.Repository

@Repository("dashboardMerchantAccountJpaAdapter")
class MerchantAccountJpaAdapter(
    private val merchantAccountCoreRepository: MerchantAccountCoreRepository,
) : MerchantAccountRepository {

    override fun save(merchantAccount: MerchantAccount): MerchantAccount {
        return merchantAccountCoreRepository.save(merchantAccount)
            ?: throw MerchantAccountNotFoundException()
    }

    override fun findByMerchantIdAndAccountId(merchantId: Long, accountId: Long): MerchantAccount? {
        return merchantAccountCoreRepository.findByMerchantIdAndAccountId(merchantId, accountId)
    }
}
