package com.goodpon.application.dashboard.merchant.port.`in`

import com.goodpon.application.dashboard.merchant.port.`in`.dto.CreateMerchantCommand
import com.goodpon.application.dashboard.merchant.port.`in`.dto.CreateMerchantResult

fun interface CreateMerchantUseCase {

    operator fun invoke(command: CreateMerchantCommand): CreateMerchantResult
}