package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.service.accessor.CouponTemplateStore
import com.goodpon.dashboard.application.coupon.service.request.CreateCouponTemplateRequest
import com.goodpon.dashboard.application.coupon.service.response.CreateCouponTemplateResponse
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccountReader
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponTemplateService(
    private val couponTemplateStore: CouponTemplateStore,
    private val merchantReader: MerchantReader,
    private val merchantAccountReader: MerchantAccountReader,
) {

    @Transactional
    fun createCouponTemplate(request: CreateCouponTemplateRequest): CreateCouponTemplateResponse {
        merchantReader.readById(request.merchantId)
        merchantAccountReader.readByMerchantIdAndAccountId(request.merchantId, request.accountId)

        val couponTemplate = request.toCouponTemplate()
        val savedCouponTemplate = couponTemplateStore.create(couponTemplate)

        return CreateCouponTemplateResponse.from(couponTemplate = savedCouponTemplate)
    }
}