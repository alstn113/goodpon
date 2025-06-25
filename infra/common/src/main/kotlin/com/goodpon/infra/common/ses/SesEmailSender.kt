package com.goodpon.infra.common.ses

import com.goodpon.core.application.auth.EmailSender
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class SesEmailSender(
    private val javaMailSender: JavaMailSender,
    private val templateRenderer: ThymeleafEmailTemplateRenderer,
) : EmailSender {

    override fun sendVerificationEmail(name: String, email: String, verificationLink: String) {
        val htmlContent = templateRenderer.render(
            "email/email-verification",
            mapOf(
                "name" to name,
                "verificationLink" to verificationLink
            )
        )

        javaMailSender.send { mimeMessage ->
            val helper = MimeMessageHelper(mimeMessage, false, "UTF-8")
            helper.setFrom("no-reply@example.com")
            helper.setTo(email)
            helper.setSubject("[GoodPon] 이메일 인증")
            helper.setText(htmlContent, true)
        }
    }
}