package com.goodpon.dashboard.infra.mail

import com.goodpon.dashboard.application.auth.port.out.VerificationEmailSender
import com.goodpon.dashboard.application.auth.port.out.dto.SendVerificationEmailRequest
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!prod")
class LogBasedVerificationEmailSender : VerificationEmailSender {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun send(request: SendVerificationEmailRequest) {
        log.info("=========================================")
        log.info("이메일 인증 요청 (실제 이메일 전송 아님)")
        log.info("이름: {}", request.name)
        log.info("이메일: {}", request.email)
        log.info("인증 링크: {}", request.verificationLink)
        log.info("=========================================")
        sentEmails.add(request)
    }

    companion object {
        val sentEmails = mutableListOf<SendVerificationEmailRequest>()
    }
}