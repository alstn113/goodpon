package com.goodpon.infra.db.jpa.adapter.dashboard

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.port.out.dto.MyMerchantDetail
import com.goodpon.dashboard.application.merchant.port.out.dto.MyMerchantSummary
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.db.jpa.core.MerchantCoreRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Repository

@Repository("dashboardMerchantJpaAdapter")
class MerchantJpaAdapter(
    private val merchantCoreRepository: MerchantCoreRepository,
) : MerchantRepository {

    override fun save(merchant: Merchant): Merchant {
        return try {
            merchantCoreRepository.save(merchant)
        } catch (e: EntityNotFoundException) {
            throw MerchantNotFoundException()
        }
    }

    override fun findById(id: Long): Merchant? {
        return merchantCoreRepository.findById(id)
    }

    override fun findMyMerchants(accountId: Long): List<MyMerchantSummary> {
        return merchantCoreRepository.findMyMerchants(accountId)
            .map { merchant ->
                MyMerchantSummary(
                    id = merchant.id,
                    name = merchant.name,
                    createdAt = merchant.createdAt,
                    updatedAt = merchant.updatedAt
                )
            }
    }

    override fun findMyMerchantDetail(accountId: Long, merchantId: Long): MyMerchantDetail? {
        val merchantDetail = merchantCoreRepository.findMyMerchantDetailDto(merchantId, accountId)
            ?: return null

        return MyMerchantDetail(
            id = merchantDetail.id,
            name = merchantDetail.name,
            clientId = merchantDetail.clientId,
            accounts = merchantDetail.accounts.map { account ->
                MyMerchantDetail.MerchantAccountDetail(
                    merchantAccountId = account.merchantAccountId,
                    accountId = account.accountId,
                    email = account.email,
                    name = account.name,
                    role = account.role,
                    createdAt = account.createdAt
                )
            },
            clientSecrets = merchantDetail.clientSecrets.map { secret ->
                MyMerchantDetail.MerchantClientSecretDetail(
                    merchantClientSecretId = secret.merchantClientSecretId,
                    secret = secret.secret,
                    createdAt = secret.createdAt,
                    expiredAt = secret.expiredAt
                )
            },
            createdAt = merchantDetail.createdAt,
            updatedAt = merchantDetail.updatedAt
        )
    }
}