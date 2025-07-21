package com.goodpon.application.dashboard.account.service

import com.goodpon.application.dashboard.account.port.`in`.dto.AccountInfo
import com.goodpon.application.dashboard.account.port.`in`.dto.SignUpResult
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