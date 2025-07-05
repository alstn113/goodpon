package com.goodpon.api.core.security

import com.goodpon.core.application.auth.MerchantPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class ApiKeyAuthenticationToken private constructor(
    private val merchantPrincipal: MerchantPrincipal,
    private val authorities: Collection<GrantedAuthority> = emptyList(),
) : AbstractAuthenticationToken(authorities) {

    init {
        isAuthenticated = true
    }

    override fun getPrincipal(): MerchantPrincipal = merchantPrincipal

    override fun getCredentials(): Any? = null

    override fun getName(): String = merchantPrincipal.merchantId.toString()

    companion object {
        fun of(
            merchantId: Long,
            authorities: Collection<GrantedAuthority> = emptyList(),
        ): ApiKeyAuthenticationToken {
            return ApiKeyAuthenticationToken(
                merchantPrincipal = MerchantPrincipal(merchantId),
                authorities = authorities,
            )
        }
    }
}