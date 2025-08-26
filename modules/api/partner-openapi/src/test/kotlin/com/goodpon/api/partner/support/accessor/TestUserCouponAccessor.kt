package com.goodpon.api.partner.support.accessor

import com.goodpon.domain.coupon.history.CouponActionType
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.domain.coupon.user.UserCouponStatus
import com.goodpon.infra.db.jpa.entity.CouponHistoryEntity
import com.goodpon.infra.db.jpa.entity.UserCouponEntity
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Component
class TestUserCouponAccessor(
    private val entityManager: EntityManager,
) {

    @Transactional
    fun issueCouponAndRecord(userId: String, couponTemplateId: Long, merchantId: Long): UserCoupon {
        val userCoupon = UserCouponEntity(
            id = UUID.randomUUID().toString().replace("-", ""),
            couponTemplateId = couponTemplateId,
            userId = userId,
            status = UserCouponStatus.ISSUED,
            issuedAt = LocalDateTime.now(),
            expiresAt = null,
            redeemedAt = null,
        )
        entityManager.persist(userCoupon)

        val couponHistory = CouponHistoryEntity(
            id = UUID.randomUUID().toString().replace("-", ""),
            userCouponId = userCoupon.id,
            actionType = CouponActionType.ISSUE,
            orderId = null,
            reason = null,
            recordedAt = LocalDateTime.now(),
        )
        entityManager.persist(couponHistory)

        return userCoupon.toDomain()
    }

    @Transactional(readOnly = true)
    fun findById(id: String): UserCouponEntity? {
        return entityManager.find(UserCouponEntity::class.java, id)
    }

    @Transactional(readOnly = true)
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