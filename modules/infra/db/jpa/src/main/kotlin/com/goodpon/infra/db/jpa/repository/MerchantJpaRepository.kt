package com.goodpon.infra.db.jpa.repository

import com.goodpon.infra.db.jpa.entity.MerchantEntity
import com.goodpon.infra.db.jpa.repository.dto.MyMerchantDetailSummaryDto
import com.goodpon.infra.db.jpa.repository.dto.MyMerchantSummaryDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MerchantJpaRepository : JpaRepository<MerchantEntity, Long> {

    fun findByClientId(clientId: String): MerchantEntity?

    @Query(
        """
        SELECT new com.goodpon.infra.db.jpa.repository.dto.MyMerchantSummaryDto(
            merchant.id,
            merchant.name,
            merchant.createdAt,
            merchant.updatedAt
        )
        FROM MerchantEntity merchant
        JOIN MerchantAccountEntity merchantAccount ON merchantAccount.merchant.id = merchant.id
        WHERE merchantAccount.accountId = :accountId
        """
    )
    fun findMyMerchants(accountId: Long): List<MyMerchantSummaryDto>

    @Query(
        """
        SELECT new com.goodpon.infra.db.jpa.repository.dto.MyMerchantDetailSummaryDto(
            merchant.id,
            merchant.name,
            merchant.clientId,
            merchant.createdAt,
            merchant.updatedAt
        )
        FROM MerchantEntity merchant
        JOIN MerchantAccountEntity merchantAccount ON merchantAccount.merchant.id = merchant.id
        WHERE merchantAccount.accountId = :accountId AND merchant.id = :merchantId
        """
    )
    fun findMyMerchantSummaryDto(merchantId: Long, accountId: Long): MyMerchantDetailSummaryDto?
}
