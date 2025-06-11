package com.goodpon.api.dashboard.api.controller.v1

import com.goodpon.api.dashboard.api.controller.v1.request.MerchantCreateWebRequest
import com.goodpon.common.domain.auth.AccountPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/merchants")
class MerchantController {

    @PostMapping
    fun createMerchant(
        @RequestBody request: MerchantCreateWebRequest,
        @AuthenticationPrincipal accountPrincipal: AccountPrincipal,
    ) {
        val appRequest = request.toAppRequest(accountPrincipal)

    }
}
