package com.goodpon.application.dashboard.merchant.port.`in`

import com.goodpon.application.dashboard.merchant.service.dto.MyMerchantSummaries

fun interface GetMyMerchantsUseCase {

    operator fun invoke(accountId: Long): MyMerchantSummaries
}
