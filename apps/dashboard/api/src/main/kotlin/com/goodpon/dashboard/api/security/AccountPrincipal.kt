package com.goodpon.dashboard.api.security

data class AccountPrincipal(
    val id: Long,
    val email: String,
    val verified: Boolean,
)