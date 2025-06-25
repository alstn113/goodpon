package com.goodpon.infra.aws.ses

import com.goodpon.core.domain.auth.EmailSender
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
            "email-verification",
            mapOf(
                "name" to name,
                "verificationLink" to verificationLink
            )
        )

        javaMailSender.send { mimeMessage ->
            val helper = MimeMessageHelper(mimeMessage, false, "UTF-8")
            helper.setFrom("no-reply@goodpon.site")
            helper.setTo(email)
            helper.setSubject("[Goodpon] 이메일 인증")
            helper.setText(htmlContent, true)
        }
    }
}