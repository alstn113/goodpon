package com.goodpon.dashboard.application.account.service

import com.goodpon.dashboard.application.account.port.`in`.GetAccountInfoUseCase
import com.goodpon.dashboard.application.account.port.`in`.dto.AccountInfo
import com.goodpon.dashboard.application.account.service.accessor.AccountAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetAccountInfoService(
    private val accountAccessor: AccountAccessor,
) : GetAccountInfoUseCase {

    @Transactional(readOnly = true)
    override fun getAccountInfo(accountId: Long): AccountInfo {
        val account = accountAccessor.readById(accountId)

        return AccountMapper.toAccountInfo(account)
    }
}