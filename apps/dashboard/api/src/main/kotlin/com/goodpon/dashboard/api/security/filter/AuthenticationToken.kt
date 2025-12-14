package com.goodpon.dashboard.api.security.filter

import com.goodpon.dashboard.api.security.AccountPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class AuthenticationToken private constructor(
    private val accountPrincipal: AccountPrincipal,
    private val authorities: Collection<GrantedAuthority> = emptyList(),
) : AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    override fun getPrincipal(): AccountPrincipal = accountPrincipal

    override fun getCredentials(): Any? = null

    override fun getName(): String = accountPrincipal.id.toString()

    companion object {
        fun of(
            id: Long,
            email: String,
            verified: Boolean,
            authorities: Collection<GrantedAuthority> = emptyList(),
        ): AuthenticationToken {
            return AuthenticationToken(
                accountPrincipal = AccountPrincipal(id, email, verified),
                authorities = authorities,
            )
        }
    }
}