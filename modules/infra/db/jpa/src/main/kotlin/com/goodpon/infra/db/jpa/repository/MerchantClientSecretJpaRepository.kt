package com.goodpon.infra.db.jpa.repository

import com.goodpon.infra.db.jpa.entity.MerchantClientSecretEntity
import com.goodpon.infra.db.jpa.repository.dto.MerchantClientSecretDetailDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MerchantClientSecretJpaRepository : JpaRepository<MerchantClientSecretEntity, Long> {

    fun findByMerchantId(merchantId: Long): List<MerchantClientSecretEntity>

    @Query(
        """
        SELECT new com.goodpon.infra.db.jpa.repository.dto.MerchantClientSecretDetailDto(
            merchantClientSecret.id,
            merchantClientSecret.secret,
            merchantClientSecret.createdAt,
            merchantClientSecret.expiredAt
        )
        FROM MerchantClientSecretEntity merchantClientSecret
        WHERE merchantClientSecret.merchant.id = :merchantId
        """
    )
    fun findMyMerchantClientSecretDetailsDto(
        merchantId: Long,
        accountId: Long,
    ): List<MerchantClientSecretDetailDto>
}