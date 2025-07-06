package com.goodpon.partner.application.coupon

import com.goodpon.partner.application.coupon.accessor.CouponTemplateStore
import com.goodpon.partner.application.coupon.request.CreateCouponTemplateRequest
import com.goodpon.partner.application.coupon.response.CreateCouponTemplateResponse
import com.goodpon.partner.application.merchant.accessor.MerchantAccountReader
import com.goodpon.partner.application.merchant.accessor.MerchantReader
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