package com.goodpon.dashboard.application.auth.port.out

import com.goodpon.domain.auth.EmailVerification

interface EmailVerificationRepository {

    fun save(verification: EmailVerification, ttlMinutes: Long = 30)

    fun findByToken(token: String): EmailVerification?

    fun delete(token: String, accountId: Long)
}
