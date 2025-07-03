package com.goodpon.core.application.auth

interface TokenProvider {
    fun generateAccessToken(accountId: Long): String
    fun getAccountId(accessToken: String): Long
}