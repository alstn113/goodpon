package com.goodpon.infra.security.apikey

import com.goodpon.core.domain.auth.MerchantPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class ApiKeyAuthenticationToken private constructor(
    private val merchantPrincipal: MerchantPrincipal,
    private val authorities: Collection<GrantedAuthority> = emptyList(),
) : AbstractAuthenticationToken(authorities) {

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

    override fun getPrincipal(): MerchantPrincipal = merchantPrincipal

    override fun getCredentials(): Any? = null

    override fun getName(): String = merchantPrincipal.merchantId.toString()
}