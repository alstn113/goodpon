package com.goodpon.api.partner.support.accessor

import com.goodpon.domain.merchant.MerchantAccountRole
import com.goodpon.infra.db.jpa.entity.AccountEntity
import com.goodpon.infra.db.jpa.entity.MerchantAccountEntity
import com.goodpon.infra.db.jpa.entity.MerchantClientSecretEntity
import com.goodpon.infra.db.jpa.entity.MerchantEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Component
class TestMerchantAccessor(
    private val entityManager: EntityManager,
) {

    @Transactional
    fun createMerchant(): Triple<Long, String, String> {
        val now = LocalDateTime.now()

        val accountEntity = AccountEntity(
            email = "test@goodpon.site",
            password = "hashedPassword",
            name = "테스트 계정",
            verified = true,
            verifiedAt = now,
        )
        entityManager.persist(accountEntity)

        val merchant = MerchantEntity(
            name = "테스트 상점",
            clientId = "ck_" + UUID.randomUUID().toString().replace("-", "")
        )
        entityManager.persist(merchant)

        val merchantClientSecret = MerchantClientSecretEntity(
            merchant = merchant,
            secret = "sk_" + UUID.randomUUID().toString().replace("-", ""),
            expiredAt = null,
        )
        entityManager.persist(merchantClientSecret)

        val merchantAccount = MerchantAccountEntity(
            merchant = merchant,
            accountId = accountEntity.id,
            role = MerchantAccountRole.OWNER
        )
        entityManager.persist(merchantAccount)

        return Triple(
            merchant.id,
            merchant.clientId,
            merchantClientSecret.secret
        )
    }
}