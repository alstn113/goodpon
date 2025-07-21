package com.goodpon.application.dashboard.coupon.service

import com.goodpon.application.dashboard.coupon.port.`in`.GetMerchantCouponTemplatesUseCase
import com.goodpon.application.dashboard.coupon.port.out.CouponTemplateRepository
import com.goodpon.application.dashboard.coupon.service.dto.CouponTemplateSummaries
import com.goodpon.application.dashboard.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.application.dashboard.merchant.service.accessor.MerchantAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMerchantCouponTemplatesService(
    private val merchantAccessor: MerchantAccessor,
    private val couponTemplateRepository: CouponTemplateRepository,
) : GetMerchantCouponTemplatesUseCase {

    @Transactional(readOnly = true)
    override fun invoke(accountId: Long, merchantId: Long): CouponTemplateSummaries {
        val merchant = merchantAccessor.readById(merchantId)
        if (!merchant.isAccessibleBy(accountId)) {
            throw NoMerchantAccessPermissionException()
        }

        val templates = couponTemplateRepository.findCouponTemplateSummaries(merchantId = merchantId)
        return CouponTemplateSummaries(templates = templates)
    }
}