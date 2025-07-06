package com.goodpon.dashboard.application.account.port.`in`

import com.goodpon.dashboard.application.account.port.`in`.dto.AccountInfo

interface GetAccountInfoUseCase {

    fun getAccountInfo(accountId: Long): AccountInfo
}