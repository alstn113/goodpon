package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.CreateCouponTemplateUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateStatsAccessor
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import com.goodpon.domain.coupon.stats.CouponTemplateStats
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateCouponTemplateService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val merchantAccessor: MerchantAccessor,
    private val couponTemplateStatsAccessor: CouponTemplateStatsAccessor,
) : CreateCouponTemplateUseCase {

    @Transactional
    override fun invoke(command: CreateCouponTemplateCommand): CreateCouponTemplateResult {
        val merchant = merchantAccessor.readById(command.merchantId)
        if (!merchant.isAccessibleBy(command.accountId)) {
            throw NoMerchantAccessPermissionException()
        }

        val couponTemplate = CouponTemplateMapper.toCouponTemplate(command)
        val savedCouponTemplate = couponTemplateAccessor.save(couponTemplate)
        val couponTemplateStats = CouponTemplateStats.create(savedCouponTemplate.id)
        couponTemplateStatsAccessor.save(couponTemplateStats)

        return CouponTemplateMapper.toCreateCouponTemplateResult(savedCouponTemplate)
    }
}