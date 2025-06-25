package com.goodpon.core.application.account.response

data class AccountInfo(
    val id: Long,
    val email: String,
    val name: String,
    val verified: Boolean,
)
