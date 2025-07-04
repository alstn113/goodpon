package com.goodpon.core.application.coupon

import com.goodpon.core.domain.UniqueIdGenerator
import com.goodpon.core.domain.coupon.user.UserCoupon
import com.goodpon.core.domain.coupon.user.UserCouponRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class UserCouponAppender(
    private val userCouponRepository: UserCouponRepository,
    private val uniqueIdGenerator: UniqueIdGenerator,
) {
    @Transactional
    fun append(
        userId: String,
        couponTemplateId: Long,
        issueAt: LocalDateTime,
        expiresAt: LocalDateTime?,
    ): UserCoupon {
        val userCoupon = UserCoupon.issue(
            id = uniqueIdGenerator.generate(),
            userId = userId,
            couponTemplateId = couponTemplateId,
            expiresAt = expiresAt,
            issueAt = issueAt
        )
        return userCouponRepository.save(userCoupon)
    }
}