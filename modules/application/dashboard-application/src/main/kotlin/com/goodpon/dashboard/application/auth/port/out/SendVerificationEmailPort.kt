package com.goodpon.dashboard.application.auth.port.out

import com.goodpon.dashboard.application.auth.port.out.dto.SendVerificationEmailRequest

interface SendVerificationEmailPort {

    fun send(request: SendVerificationEmailRequest)
}