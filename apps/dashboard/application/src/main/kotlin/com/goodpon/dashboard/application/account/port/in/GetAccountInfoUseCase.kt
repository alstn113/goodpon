package com.goodpon.dashboard.application.account.port.`in`

import com.goodpon.dashboard.application.account.port.`in`.dto.AccountInfo


fun interface GetAccountInfoUseCase {

    operator fun invoke(accountId: Long): AccountInfo
}