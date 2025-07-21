package com.goodpon.application.dashboard.auth.port.out

import com.goodpon.application.dashboard.auth.port.out.dto.SendVerificationEmailRequest

interface VerificationEmailSender {

    fun send(request: SendVerificationEmailRequest)
}