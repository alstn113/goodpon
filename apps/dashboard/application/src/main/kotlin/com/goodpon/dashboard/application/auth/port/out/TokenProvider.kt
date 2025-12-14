package com.goodpon.dashboard.application.auth.port.out

interface TokenProvider {

    fun generateAccessToken(accountId: Long): String

    fun getAccountId(accessToken: String): Long
}