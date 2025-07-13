package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.GetMerchantCouponTemplatesUseCase
import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateSummary
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMerchantCouponTemplatesService(
    private val merchantAccessor: MerchantAccessor,
    private val couponTemplateRepository: CouponTemplateRepository,
) : GetMerchantCouponTemplatesUseCase {

    @Transactional(readOnly = true)
    override fun getMerchantCouponTemplates(accountId: Long, merchantId: Long): List<CouponTemplateSummary> {
        val merchant = merchantAccessor.readById(merchantId)
        if (!merchant.isAccessibleBy(accountId)) {
            throw NoMerchantAccessPermissionException()
        }

        return couponTemplateRepository.findCouponTemplateSummaries(merchantId = merchantId)
    }
}