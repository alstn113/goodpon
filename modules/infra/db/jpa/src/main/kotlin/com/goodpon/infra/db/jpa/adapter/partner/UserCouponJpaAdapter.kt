package com.goodpon.infra.db.jpa.adapter.partner

import com.goodpon.application.partner.coupon.port.out.UserCouponRepository
import com.goodpon.application.partner.coupon.service.dto.AvailableUserCouponView
import com.goodpon.application.partner.coupon.service.dto.UserCouponSummary
import com.goodpon.domain.coupon.user.UserCoupon
import com.goodpon.infra.db.jpa.core.UserCouponCoreRepository
import org.springframework.stereotype.Repository

@Repository("partnerUserCouponJpaAdapter")
class UserCouponJpaAdapter(
    private val userCouponCoreRepository: UserCouponCoreRepository,
) : UserCouponRepository {

    override fun save(userCoupon: UserCoupon): UserCoupon {
        return userCouponCoreRepository.save(userCoupon)
    }

    override fun saveAndFlush(userCoupon: UserCoupon): UserCoupon {
        return userCouponCoreRepository.saveAndFlush(userCoupon)
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