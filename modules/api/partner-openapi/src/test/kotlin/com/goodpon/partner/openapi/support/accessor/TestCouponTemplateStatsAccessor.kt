package com.goodpon.partner.openapi.support.accessor

import com.goodpon.infra.db.jpa.entity.CouponTemplateStatsEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component

@Component
class TestCouponTemplateStatsAccessor(
    private val entityManager: EntityManager,
) {

    fun findById(couponTemplateId: Long): CouponTemplateStatsEntity? {
        return entityManager.find(CouponTemplateStatsEntity::class.java, couponTemplateId)
    }
}