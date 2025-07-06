package com.goodpon.infra.aws.ses

import com.goodpon.domain.auth.EmailSender
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sesv2.SesV2Client
import software.amazon.awssdk.services.sesv2.model.*

@Component
class SesEmailSender(
    private val sesV2Client: SesV2Client,
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

        val destination = Destination.builder()
            .toAddresses(email)
            .build()
        val subject = Content.builder()
            .charset("UTF-8")
            .data("[Goodpon] 이메일 인증")
            .build()
        val bodyContent = Content.builder()
            .charset("UTF-8")
            .data(htmlContent)
            .build()
        val body = Body.builder()
            .html(bodyContent)
            .build()
        val message = Message.builder()
            .subject(subject)
            .body(body)
            .build()
        val emailContent = EmailContent.builder()
            .simple(message)
            .build()
        val request = SendEmailRequest.builder()
            .fromEmailAddress("no-reply@goodpon.site")
            .destination(destination)
            .content(emailContent)
            .build()

        sesV2Client.sendEmail(request)
    }
}