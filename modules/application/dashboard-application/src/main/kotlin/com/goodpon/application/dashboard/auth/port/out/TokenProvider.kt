package com.goodpon.application.dashboard.auth.port.out

interface TokenProvider {

    fun generateAccessToken(accountId: Long): String

    fun getAccountId(accessToken: String): Long
}