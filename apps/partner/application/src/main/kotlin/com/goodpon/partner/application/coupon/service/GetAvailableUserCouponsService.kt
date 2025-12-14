package com.goodpon.partner.application.coupon.service

import com.goodpon.partner.application.coupon.port.`in`.GetAvailableUserCouponsUseCase
import com.goodpon.partner.application.coupon.port.`in`.dto.GetAvailableUserCouponsQuery
import com.goodpon.partner.application.coupon.port.out.CouponStatsStore
import com.goodpon.partner.application.coupon.port.out.UserCouponRepository
import com.goodpon.partner.application.coupon.service.dto.AvailableUserCouponsView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetAvailableUserCouponsService(
    private val userCouponRepository: UserCouponRepository,
    private val couponStatsStore: CouponStatsStore,
) : GetAvailableUserCouponsUseCase {

    @Transactional(readOnly = true)
    override fun invoke(query: GetAvailableUserCouponsQuery): AvailableUserCouponsView {
        val coupons = userCouponRepository.findAvailableUserCouponsView(
            userId = query.userId,
            merchantId = query.merchantId,
            orderAmount = query.orderAmount
        )
        val couponTemplateIds = coupons.map { it.couponTemplateId }.toList()
        val statsMap = couponStatsStore.getMultipleStats(couponTemplateIds)
        val availableCoupons = coupons.filter {
            val (_, redeemCount) = statsMap[it.couponTemplateId] ?: (0L to 0L)
            it.maxRedeemCount == null || redeemCount < it.maxRedeemCount
        }

        return AvailableUserCouponsView(coupons = availableCoupons)
    }
}