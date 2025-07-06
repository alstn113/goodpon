package com.goodpon.dashboard.application.auth.service

interface TokenProvider {
    fun generateAccessToken(accountId: Long): String
    fun getAccountId(accessToken: String): Long
}