package com.goodpon.common.application.account.response

import com.goodpon.common.domain.account.AccountStatus

data class AccountInfo(
    val accountId: Long,
    val email: String,
    val name: String,
    val status: AccountStatus,
)
