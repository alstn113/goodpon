package com.goodpon.dashboard.infra.persistence

import com.goodpon.dashboard.application.merchant.port.out.MerchantRepository
import com.goodpon.dashboard.application.merchant.port.out.exception.MerchantNotFoundException
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantDetail
import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantSummary
import com.goodpon.domain.merchant.Merchant
import com.goodpon.infra.jpa.core.MerchantCoreRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Repository

@Repository
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
                )
            }
    }

    override fun findMyMerchantDetail(accountId: Long, merchantId: Long): MyMerchantDetail? {
        val dto = merchantCoreRepository
            .findMyMerchantDetail(accountId = accountId, merchantId = merchantId) ?: return null

        return MyMerchantDetail(
            id = dto.id,
            name = dto.name,
            clientId = dto.clientId,
            merchantAccounts = dto.merchantAccounts.map { account ->
                MyMerchantDetail.MerchantAccountDetail(
                    merchantAccountId = account.merchantAccountId,
                    accountId = account.accountId,
                    email = account.email,
                    name = account.name,
                    role = account.role,
                    createdAt = account.createdAt
                )
            },
            clientSecrets = dto.clientSecrets.map { secret ->
                MyMerchantDetail.MerchantClientSecretDetail(
                    merchantClientSecretId = secret.merchantClientSecretId,
                    secret = secret.secret,
                    createdAt = secret.createdAt,
                    expiredAt = secret.expiredAt
                )
            },
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }
}