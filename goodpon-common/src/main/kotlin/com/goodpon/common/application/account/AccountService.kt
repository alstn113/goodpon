package com.goodpon.common.application.account

import com.goodpon.common.application.account.response.AccountInfo
import com.goodpon.common.domain.account.AccountReader
import com.goodpon.common.support.error.CoreException
import com.goodpon.common.support.error.ErrorType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountReader: AccountReader,
) {

    @Transactional(readOnly = true)
    fun getAccountInfo(accountId: Long): AccountInfo {
        val account = accountReader.readById(accountId)

        if (account.isPending()) {
            throw CoreException(ErrorType.ACCOUNT_PENDING)
        }

        return AccountInfo(
            accountId = account.id,
            email = account.email,
            name = account.name
        )
    }
}