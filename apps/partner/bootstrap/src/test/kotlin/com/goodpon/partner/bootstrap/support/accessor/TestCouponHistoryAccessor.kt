package com.goodpon.partner.bootstrap.support.accessor

import com.goodpon.infra.jpa.entity.CouponHistoryEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TestCouponHistoryAccessor(
    private val entityManager: EntityManager,
) {

    @Transactional(readOnly = true)
    fun findAll(): List<CouponHistoryEntity> {
        return entityManager.createQuery(
            "SELECT ch FROM CouponHistoryEntity ch",
            CouponHistoryEntity::class.java
        ).resultList
    }

    @Transactional(readOnly = true)
    fun findByUserCouponId(userCouponId: String): List<CouponHistoryEntity> {
        return entityManager.createQuery(
            "SELECT ch FROM CouponHistoryEntity ch WHERE ch.userCouponId = :userCouponId",
            CouponHistoryEntity::class.java
        ).setParameter("userCouponId", userCouponId).resultList
    }
}