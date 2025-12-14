package com.goodpon.partner.infra.persistence

import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.infra.jpa.core.UserCouponCoreRepository
import com.goodpon.partner.application.coupon.port.out.UserCouponRepository
import com.goodpon.partner.application.coupon.service.dto.AvailableUserCouponView
import com.goodpon.partner.application.coupon.service.dto.UserCouponSummary
import org.springframework.stereotype.Repository

@Repository
class UserCouponJpaAdapter(
    private val userCouponCoreRepository: UserCouponCoreRepository,
) : UserCouponRepository {

    override fun save(userCoupon: UserCoupon): UserCoupon {
        return userCouponCoreRepository.save(userCoupon)
    }

    override fun findByIdForUpdate(id: String): UserCoupon? {
        return userCouponCoreRepository.findByIdForUpdate(id)
    }

    override fun existsByUserIdAndCouponTemplateId(userId: String, couponTemplateId: Long): Boolean {
        return userCouponCoreRepository.existsByUserIdAndCouponTemplateId(userId, couponTemplateId)
    }

    override fun findUserCouponSummaries(userId: String, merchantId: Long): List<UserCouponSummary> {
        return userCouponCoreRepository.findUserCouponSummaries(userId = userId, merchantId = merchantId)
            .map {
                UserCouponSummary(
                    userCouponId = it.userCouponId,
                    couponTemplateId = it.couponTemplateId,
                    couponTemplateName = it.couponTemplateName,
                    couponTemplateDescription = it.couponTemplateDescription,
                    discountType = it.discountType,
                    discountValue = it.discountValue,
                    maxDiscountAmount = it.maxDiscountAmount,
                    minOrderAmount = it.minOrderAmount,
                    issuedAt = it.issuedAt,
                    expiresAt = it.expiresAt,
                    limitType = it.limitType,
                    maxIssueCount = it.maxIssueCount,
                    maxRedeemCount = it.maxRedeemCount,
                )
            }
    }

    override fun findAvailableUserCouponsView(
        userId: String,
        merchantId: Long,
        orderAmount: Int,
    ): List<AvailableUserCouponView> {
        return userCouponCoreRepository.findAvailableUserCouponsForOrderAmount(
            userId = userId,
            merchantId = merchantId,
            orderAmount = orderAmount
        ).map {
            AvailableUserCouponView(
                userCouponId = it.userCouponId,
                couponTemplateId = it.couponTemplateId,
                couponTemplateName = it.couponTemplateName,
                couponTemplateDescription = it.couponTemplateDescription,
                discountType = it.discountType,
                discountValue = it.discountValue,
                maxDiscountAmount = it.maxDiscountAmount,
                minOrderAmount = it.minOrderAmount,
                issuedAt = it.issuedAt,
                expiresAt = it.expiresAt,
                limitType = it.limitType,
                maxIssueCount = it.maxIssueCount,
                maxRedeemCount = it.maxRedeemCount,
                isMinOrderAmountReached = it.isMinOrderAmountReached
            )
        }
    }
}