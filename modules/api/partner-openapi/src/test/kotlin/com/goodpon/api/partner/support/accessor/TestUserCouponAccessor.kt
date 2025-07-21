package com.goodpon.api.partner.support.accessor

import com.goodpon.infra.db.jpa.entity.UserCouponEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component

@Component
class TestUserCouponAccessor(
    private val entityManager: EntityManager,
) {

    fun findById(id: String): UserCouponEntity? {
        return entityManager.find(UserCouponEntity::class.java, id)
    }

    fun findByCouponTemplateId(
        couponTemplateId: Long,
    ): List<UserCouponEntity> {
        return entityManager.createQuery(
            "SELECT uc FROM UserCouponEntity uc WHERE uc.couponTemplateId = :couponTemplateId",
            UserCouponEntity::class.java,
        )
            .setParameter("couponTemplateId", couponTemplateId)
            .resultList
    }
}