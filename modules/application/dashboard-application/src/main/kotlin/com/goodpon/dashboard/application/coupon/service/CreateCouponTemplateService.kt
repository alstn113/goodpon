package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.CreateCouponTemplateUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateCommand
import com.goodpon.dashboard.application.coupon.port.`in`.dto.CreateCouponTemplateResult
import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateStore
import com.goodpon.dashboard.application.merchant.port.out.exception.UnauthorizedMerchantAccountException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateCouponTemplateService(
    private val couponTemplateStore: CouponTemplateStore,
    private val merchantReader: MerchantReader,
) : CreateCouponTemplateUseCase {

    @Transactional
    override fun createCouponTemplate(command: CreateCouponTemplateCommand): CreateCouponTemplateResult {
        val merchant = merchantReader.readById(command.merchantId)
        if (merchant.hasAccount(command.accountId)) {
            throw UnauthorizedMerchantAccountException()
        }

        val couponTemplate = CouponTemplateMapper.toCouponTemplate(command)
        val savedCouponTemplate = couponTemplateStore.create(couponTemplate)

        return CouponTemplateMapper.toCreateCouponTemplateResult(savedCouponTemplate)
    }
}