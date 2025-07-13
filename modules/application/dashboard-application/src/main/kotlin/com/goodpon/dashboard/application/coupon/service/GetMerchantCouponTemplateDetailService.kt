package com.goodpon.dashboard.application.coupon.service

import com.goodpon.dashboard.application.coupon.port.`in`.GetMerchantCouponTemplateDetailUseCase
import com.goodpon.dashboard.application.coupon.port.`in`.dto.GetMerchantCouponTemplateDetail
import com.goodpon.dashboard.application.coupon.port.out.CouponTemplateRepository
import com.goodpon.dashboard.application.coupon.port.out.exception.CouponTemplateNotFoundException
import com.goodpon.dashboard.application.coupon.service.dto.CouponTemplateDetail
import com.goodpon.dashboard.application.coupon.service.exception.CouponTemplateNotOwnedByMerchantException
import com.goodpon.dashboard.application.coupon.service.exception.NoMerchantAccessPermissionException
import com.goodpon.dashboard.application.merchant.service.accessor.MerchantAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetMerchantCouponTemplateDetailService(
    private val merchantAccessor: MerchantAccessor,
    private val couponTemplateRepository: CouponTemplateRepository,
) : GetMerchantCouponTemplateDetailUseCase {

    @Transactional(readOnly = true)
    override fun getMerchantCouponTemplateDetail(query: GetMerchantCouponTemplateDetail): CouponTemplateDetail {
        val merchant = merchantAccessor.readById(query.merchantId)
        if (!merchant.isAccessibleBy(query.accountId)) {
            throw NoMerchantAccessPermissionException()
        }

        val couponTemplateDetail = couponTemplateRepository
            .findCouponTemplateDetail(couponTemplateId = query.couponTemplateId)
            ?: throw CouponTemplateNotFoundException()

        val isNotOwnedByMerchant = couponTemplateDetail.merchantId != query.merchantId
        if (isNotOwnedByMerchant) {
            throw CouponTemplateNotOwnedByMerchantException()
        }

        return couponTemplateDetail
    }
}