package com.goodpon.dashboard.application.auth.port.out

import com.goodpon.dashboard.application.auth.port.out.dto.SendVerificationEmailRequest

interface VerificationEmailSender {

    fun send(request: SendVerificationEmailRequest)
}