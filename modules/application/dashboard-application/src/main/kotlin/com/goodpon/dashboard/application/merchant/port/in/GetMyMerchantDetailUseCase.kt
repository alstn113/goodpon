package com.goodpon.dashboard.application.merchant.port.`in`

import com.goodpon.dashboard.application.merchant.port.out.dto.MyMerchantDetail

interface GetMyMerchantDetailUseCase {

    fun getMyMerchantDetail(accountId: Long, merchantId: Long): MyMerchantDetail
}