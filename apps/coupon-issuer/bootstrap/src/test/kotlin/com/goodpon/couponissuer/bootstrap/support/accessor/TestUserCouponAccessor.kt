package com.goodpon.couponissuer.bootstrap.support.accessor

import com.goodpon.infra.jpa.entity.UserCouponEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TestUserCouponAccessor(
    private val entityManager: EntityManager,
) {

    @Transactional(readOnly = true)
    fun findAll(): List<UserCouponEntity> {
        return entityManager.createQuery(
            "SELECT uc FROM UserCouponEntity uc",
            UserCouponEntity::class.java
        ).resultList
    }
}