package io.github.alstn113.goodpon.domain.auth

interface VerificationTokenService {

    fun generateToken(accountId: Long): String
    fun validateAndConsumeToken(token: String): Long
}