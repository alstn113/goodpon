package com.goodpon.application.dashboard.account.service

import com.goodpon.application.dashboard.account.port.`in`.GetAccountInfoUseCase
import com.goodpon.application.dashboard.account.port.`in`.dto.AccountInfo
import com.goodpon.application.dashboard.account.service.accessor.AccountAccessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetAccountInfoService(
    private val accountAccessor: AccountAccessor,
) : GetAccountInfoUseCase {

    @Transactional(readOnly = true)
    override fun invoke(accountId: Long): AccountInfo {
        val account = accountAccessor.readById(accountId)

        return AccountMapper.toAccountInfo(account)
    }
}