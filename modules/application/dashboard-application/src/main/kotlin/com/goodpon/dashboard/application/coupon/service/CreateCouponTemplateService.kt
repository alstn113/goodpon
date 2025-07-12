package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.CreateCouponTemplateUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateAccessor
import com.goodpon.dashboard.application.merchant.port.out.exception.UnauthorizedMerchantAccountException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateCouponTemplateService(
    private val couponTemplateAccessor: CouponTemplateAccessor,
    private val merchantAccessor: MerchantAccessor,
) : CreateCouponTemplateUseCase {

    @Transactional
    override fun createCouponTemplate(command: CreateCouponTemplateCommand): CreateCouponTemplateResult {
        val merchant = merchantAccessor.readById(command.merchantId)
        if (merchant.hasAccount(command.accountId)) {
            throw UnauthorizedMerchantAccountException()
        }

        val couponTemplate = CouponTemplateMapper.toCouponTemplate(command)
        val savedCouponTemplate = couponTemplateAccessor.create(couponTemplate)

        return CouponTemplateMapper.toCreateCouponTemplateResult(savedCouponTemplate)
    }
}