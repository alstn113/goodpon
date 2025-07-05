package com.goodpon.api.dashboard.security

data class AccountPrincipal(
    val id: Long,
    val email: String,
    val verified: Boolean,
)