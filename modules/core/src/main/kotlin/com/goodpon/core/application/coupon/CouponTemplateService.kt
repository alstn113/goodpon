package com.goodpon.core.application.coupon

import com.goodpon.core.application.coupon.request.CreateCouponTemplateRequest
import com.goodpon.core.application.coupon.response.CreateCouponTemplateResponse
import com.goodpon.core.application.merchant.accessor.MerchantAccountReader
import com.goodpon.core.application.merchant.accessor.MerchantReader
import com.goodpon.core.domain.coupon.template.CouponTemplateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponTemplateService(
    private val couponTemplateRepository: CouponTemplateRepository,
    private val merchantReader: MerchantReader,
    private val merchantAccountReader: MerchantAccountReader,
) {
    @Transactional
    fun createCouponTemplate(request: CreateCouponTemplateRequest): CreateCouponTemplateResponse {
        merchantReader.readById(request.merchantId)
        merchantAccountReader.readByMerchantIdAndAccountId(request.merchantId, request.accountPrincipal.id)

        val couponTemplate = request.toCouponTemplate()
        val savedCouponTemplate = couponTemplateRepository.save(couponTemplate)

        return CreateCouponTemplateResponse.from(couponTemplate = savedCouponTemplate)
    }
}