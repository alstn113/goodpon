package com.goodpon.dashboard.application.merchant.port.`in`

import com.goodpon.dashboard.application.merchant.service.dto.MyMerchantSummaries

fun interface GetMyMerchantsUseCase {

    operator fun invoke(accountId: Long): MyMerchantSummaries
}
