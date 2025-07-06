package com.goodpon.dashboard.application.merchant.port.`in`

import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantCommand
import com.goodpon.dashboard.application.merchant.port.`in`.dto.CreateMerchantResult

interface CreateMerchantUseCase {

    fun createMerchant(command: CreateMerchantCommand): CreateMerchantResult
}