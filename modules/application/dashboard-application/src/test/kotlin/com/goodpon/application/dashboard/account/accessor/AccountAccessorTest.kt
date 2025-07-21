package com.goodpon.application.dashboard.account.accessor

import com.goodpon.application.dashboard.account.port.out.AccountRepository
import com.goodpon.application.dashboard.account.port.out.exception.AccountNotFoundException
import com.goodpon.application.dashboard.account.service.accessor.AccountAccessor
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk

class AccountAccessorTest : DescribeSpec({

    val accountRepository = mockk<AccountRepository>()
    val accountAccessor = AccountAccessor(accountRepository)

    describe("readById") {
        it("계정 식별자로 계정을 조회할 수 없는 경우 예외를 발생시킨다.") {
            every {
                accountRepository.findById(any())
            } returns null

            shouldThrow<AccountNotFoundException> {
                accountAccessor.readById(1L)
            }
        }
    }

    describe("readByEmail") {
        it("계정 이메일로 계정을 조회할 수 없는 경우 예외를 발생시킨다.") {
            every {
                accountRepository.findByEmail(any())
            } returns null

            shouldThrow<AccountNotFoundException> {
                accountAccessor.readByEmail("notExists@gmail.com")
            }
        }
    }
})