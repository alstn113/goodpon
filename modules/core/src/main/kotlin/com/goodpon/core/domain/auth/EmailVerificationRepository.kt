package com.goodpon.core.domain.auth

interface EmailVerificationRepository {
    fun save(verification: EmailVerification, ttlMinutes: Long = 30)
    fun findByToken(token: String): EmailVerification?
    fun delete(token: String, accountId: Long)
}