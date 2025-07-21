package com.goodpon.application.dashboard.account.port.`in`

import com.goodpon.application.dashboard.account.port.`in`.dto.AccountInfo

fun interface GetAccountInfoUseCase {

    operator fun invoke(accountId: Long): AccountInfo
}