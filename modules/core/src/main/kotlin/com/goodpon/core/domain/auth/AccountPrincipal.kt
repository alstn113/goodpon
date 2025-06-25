package com.goodpon.core.domain.auth

data class AccountPrincipal(
    val id: Long,
    val email: String,
    val verified: Boolean,
)