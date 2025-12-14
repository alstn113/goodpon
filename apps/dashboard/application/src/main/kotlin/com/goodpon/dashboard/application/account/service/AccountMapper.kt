package com.goodpon.dashboard.application.account.service

import com.goodpon.dashboard.application.account.port.`in`.dto.AccountInfo
import com.goodpon.dashboard.application.account.port.`in`.dto.SignUpResult
import com.goodpon.domain.account.Account

object AccountMapper {

    fun toAccountInfo(account: Account): AccountInfo {
        return AccountInfo(
            id = account.id,
            email = account.email.value,
            name = account.name.value,
            verified = account.verified,
        )
    }

    fun toSignUpResult(account: Account): SignUpResult {
        return SignUpResult(
            id = account.id,
            email = account.email.value,
            name = account.name.value,
            verified = account.verified,
        )
    }
}