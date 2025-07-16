package com.goodpon.partner.openapi.support.accessor

import com.goodpon.infra.db.jpa.entity.CouponHistoryEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component

@Component
class TestCouponHistoryAccessor(
    private val entityManager: EntityManager,
) {

    fun findAll(): List<CouponHistoryEntity> {
        return entityManager.createQuery(
            "SELECT ch FROM CouponHistoryEntity ch",
            CouponHistoryEntity::class.java
        ).resultList
    }

    fun findByUserCouponId(userCouponId: String): List<CouponHistoryEntity> {
        return entityManager.createQuery(
            "SELECT ch FROM CouponHistoryEntity ch WHERE ch.userCouponId = :userCouponId",
            CouponHistoryEntity::class.java
        ).setParameter("userCouponId", userCouponId).resultList
    }
}