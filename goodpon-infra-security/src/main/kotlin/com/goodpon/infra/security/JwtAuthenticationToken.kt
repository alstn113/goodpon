package com.goodpon.infra.security

import com.goodpon.common.domain.auth.AccountPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class JwtAuthenticationToken private constructor(
    private val accountPrincipal: AccountPrincipal,
    private val authorities: Collection<GrantedAuthority> = emptyList(),
) : AbstractAuthenticationToken(authorities) {

    companion object {
        fun of(
            accountId: Long,
            authorities: Collection<GrantedAuthority> = emptyList(),
        ): JwtAuthenticationToken {
            return JwtAuthenticationToken(
                accountPrincipal = AccountPrincipal(accountId),
                authorities = authorities,
            )
        }
    }

    override fun getPrincipal(): AccountPrincipal = accountPrincipal

    override fun getCredentials(): Any? = null

    override fun getName(): String = accountPrincipal.accountId.toString()
}