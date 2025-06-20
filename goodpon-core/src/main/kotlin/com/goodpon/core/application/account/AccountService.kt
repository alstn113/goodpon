package com.goodpon.core.application.account

import com.goodpon.core.application.account.response.AccountInfo
import com.goodpon.core.domain.account.AccountReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountReader: AccountReader,
) {

    @Transactional(readOnly = true)
    fun getAccountInfo(accountId: Long): AccountInfo {
        val account = accountReader.readById(accountId)

        return AccountInfo(
            accountId = account.id,
            email = account.email,
            name = account.name,
            status = account.status,
        )
    }
}