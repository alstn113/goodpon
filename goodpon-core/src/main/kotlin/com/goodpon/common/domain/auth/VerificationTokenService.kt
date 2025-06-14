package com.goodpon.common.domain.auth

interface VerificationTokenService {

    fun generateToken(accountId: Long): String
    fun validateAndConsumeToken(token: String): Long
}