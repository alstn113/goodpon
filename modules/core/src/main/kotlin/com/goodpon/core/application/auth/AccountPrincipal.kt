package com.goodpon.core.application.auth

data class AccountPrincipal(
    val id: Long,
    val email: String,
    val verified: Boolean,
)