package com.goodpon.core.application.account.response

import com.goodpon.core.domain.account.AccountStatus

data class AccountInfo(
    val accountId: Long,
    val email: String,
    val name: String,
    val status: AccountStatus,
)
