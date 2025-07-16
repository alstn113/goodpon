package com.goodpon.infra.db.jpa.repository

import com.goodpon.infra.db.jpa.entity.MerchantAccountEntity
import com.goodpon.infra.db.jpa.repository.dto.MerchantAccountDetailDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MerchantAccountJpaRepository : JpaRepository<MerchantAccountEntity, Long> {

    fun findByMerchantId(merchantId: Long): List<MerchantAccountEntity>

    @Query(
        """
        SELECT new com.goodpon.infra.db.jpa.repository.dto.MerchantAccountDetailDto(
            merchantAccount.id,
            account.id,
            account.email,
            account.name, 
            merchantAccount.role,
            merchantAccount.createdAt
        )
        FROM MerchantAccountEntity merchantAccount
        JOIN AccountEntity account ON merchantAccount.accountId = account.id
        WHERE merchantAccount.merchant.id = :merchantId 
            AND merchantAccount.accountId = :accountId
        ORDER BY merchantAccount.createdAt DESC
    """
    )
    fun findMyMerchantAccountDetailsDto(
        merchantId: Long,
        accountId: Long,
    ): List<MerchantAccountDetailDto>
}