package com.goodpon.dashboard.application.merchant.port.`in`

import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantCommand
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantResult

fun interface CreateMerchantUseCase {

    operator fun invoke(command: CreateMerchantCommand): CreateMerchantResult
}