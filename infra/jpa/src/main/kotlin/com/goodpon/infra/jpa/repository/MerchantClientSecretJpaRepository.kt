package com.goodpon.infra.jpa.repository

import com.goodpon.infra.jpa.entity.MerchantClientSecretEntity
import com.goodpon.infra.jpa.repository.dto.MerchantClientSecretDetailDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MerchantClientSecretJpaRepository : JpaRepository<MerchantClientSecretEntity, Long> {

    fun findByMerchantId(merchantId: Long): List<MerchantClientSecretEntity>

    @Query(
        """
        SELECT new com.goodpon.infra.jpa.repository.dto.MerchantClientSecretDetailDto(
            merchantClientSecret.id,
            merchantClientSecret.secret,
            merchantClientSecret.createdAt,
            merchantClientSecret.expiredAt
        )
        FROM MerchantClientSecretEntity merchantClientSecret
        WHERE merchantClientSecret.merchant.id = :merchantId
        ORDER BY merchantClientSecret.createdAt DESC
        """
    )
    fun findMyMerchantClientSecretDetailsDto(merchantId: Long): List<MerchantClientSecretDetailDto>
}