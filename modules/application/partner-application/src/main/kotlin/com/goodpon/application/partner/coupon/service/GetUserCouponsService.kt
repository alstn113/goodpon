package com.goodpon.application.partner.coupon.service

import com.goodpon.application.partner.coupon.port.`in`.GetUserCouponsUseCase
import com.goodpon.application.partner.coupon.port.out.CouponTemplateStatsCache
import com.goodpon.application.partner.coupon.port.out.UserCouponRepository
import com.goodpon.application.partner.coupon.service.dto.UserCouponList
import com.goodpon.application.partner.coupon.service.dto.UserCouponWithRedeemable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUserCouponsService(
    private val userCouponRepository: UserCouponRepository,
    private val couponTemplateStatsCache: CouponTemplateStatsCache,
) : GetUserCouponsUseCase {

    @Transactional(readOnly = true)
    override fun invoke(merchantId: Long, userId: String): UserCouponList {
        val summaries = userCouponRepository.findUserCouponSummaries(
            userId = userId,
            merchantId = merchantId
        )
        val couponTemplateIds = summaries.map { it.couponTemplateId }.toList()
        val statsMap = couponTemplateStatsCache.getMultipleStats(couponTemplateIds)
        val coupons = summaries.map {
            val (_, redeemCount) = statsMap[it.couponTemplateId] ?: (0L to 0L)
            val isRedeemable = it.maxRedeemCount == null || redeemCount < it.maxRedeemCount
            it.withRedeemable(isRedeemable)
        }.sortedWith(
            compareByDescending<UserCouponWithRedeemable> { it.isRedeemable }
                .thenBy { it.expiresAt }
                .thenByDescending { it.issuedAt }
        )

        return UserCouponList(coupons = coupons)
    }
}