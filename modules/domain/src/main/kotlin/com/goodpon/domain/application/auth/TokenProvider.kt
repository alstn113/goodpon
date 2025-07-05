package com.goodpon.domain.application.auth

interface TokenProvider {
    fun generateAccessToken(accountId: Long): String
    fun getAccountId(accessToken: String): Long
}