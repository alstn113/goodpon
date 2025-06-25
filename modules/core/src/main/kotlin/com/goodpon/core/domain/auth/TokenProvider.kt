package com.goodpon.core.domain.auth

interface TokenProvider {

    fun generateAccessToken(accountId: Long): String
    fun getAccountId(accessToken: String): Long
}