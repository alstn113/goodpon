package com.goodpon.core.domain.account

import com.goodpon.core.domain.account.exception.AccountAlreadyVerifiedException
import com.goodpon.core.domain.account.vo.AccountEmail
import com.goodpon.core.domain.account.vo.AccountName
import com.goodpon.core.domain.account.vo.AccountPassword
import java.time.LocalDateTime

data class Account private constructor(
    val id: Long,
    val email: AccountEmail,
    val password: AccountPassword,
    val name: AccountName,
    val verified: Boolean = false,
    val verifiedAt: LocalDateTime? = null,
) {

    fun verify(verifiedAt: LocalDateTime): Account {
        if (verified) {
            throw AccountAlreadyVerifiedException()
        }

        return this.copy(
            verified = true,
            verifiedAt = verifiedAt
        )
    }

    companion object {

        fun create(email: String, password: String, name: String): Account {
            return Account(
                id = 0,
                email = AccountEmail(email),
                password = AccountPassword(password),
                name = AccountName(name)
            )
        }

        fun reconstruct(
            id: Long,
            email: String,
            password: String,
            name: String,
            verified: Boolean,
            verifiedAt: LocalDateTime?,
        ): Account {
            return Account(
                id = id,
                email = AccountEmail(email),
                password = AccountPassword(password),
                name = AccountName(name),
                verified = verified,
                verifiedAt = verifiedAt
            )
        }
    }
}