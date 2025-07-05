package com.goodpon.partner.openapi.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class ApiKeyAuthenticationToken private constructor(
    private val merchantPrincipal: com.goodpon.partner.openapi.security.MerchantPrincipal,
    private val authorities: Collection<GrantedAuthority> = emptyList(),
) : AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    override fun getPrincipal(): com.goodpon.partner.openapi.security.MerchantPrincipal = merchantPrincipal

    override fun getCredentials(): Any? = null

    override fun getName(): String = merchantPrincipal.id.toString()

    companion object {
        fun of(
            merchantId: Long,
            authorities: Collection<GrantedAuthority> = emptyList(),
        ): com.goodpon.partner.openapi.security.ApiKeyAuthenticationToken {
            return com.goodpon.partner.openapi.security.ApiKeyAuthenticationToken(
                merchantPrincipal = com.goodpon.partner.openapi.security.MerchantPrincipal(merchantId),
                authorities = authorities,
            )
        }
    }
}