package com.goodpon.infra.security.jwt

import com.goodpon.core.domain.auth.AccountPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class JwtAuthenticationToken private constructor(
    private val accountPrincipal: AccountPrincipal,
    private val authorities: Collection<GrantedAuthority> = emptyList(),
) : AbstractAuthenticationToken(authorities) {

    override fun getPrincipal(): AccountPrincipal = accountPrincipal

    override fun getCredentials(): Any? = null

    override fun getName(): String = accountPrincipal.id.toString()

    companion object {
        fun of(
            id: Long,
            email: String,
            authorities: Collection<GrantedAuthority> = emptyList(),
        ): JwtAuthenticationToken {
            return JwtAuthenticationToken(
                accountPrincipal = AccountPrincipal(id, email),
                authorities = authorities,
            )
        }
    }
}