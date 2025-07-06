package com.goodpon.dashboard.application.account.port.`in`.dto

data class AccountInfo(
    val id: Long,
    val email: String,
    val name: String,
    val verified: Boolean,
)
