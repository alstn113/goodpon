package com.goodpon.infra.aws.ses

import com.goodpon.dashboard.application.auth.port.out.VerificationEmailSender
import com.goodpon.dashboard.application.auth.port.out.dto.SendVerificationEmailRequest
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sesv2.SesV2Client
import software.amazon.awssdk.services.sesv2.model.*

@Component
@Profile("prod")
class SesVerificationEmailSender(
    private val sesV2Client: SesV2Client,
    private val templateRenderer: ThymeleafEmailTemplateRenderer,
) : VerificationEmailSender {

    override fun send(request: SendVerificationEmailRequest) {
        val htmlContent = templateRenderer.render(
            "email-verification",
            mapOf(
                "name" to request.name,
                "verificationLink" to request.verificationLink,
            )
        )

        val destination = Destination.builder()
            .toAddresses(request.email)
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
        val sendEmailRequest = SendEmailRequest.builder()
            .fromEmailAddress("no-reply@goodpon.site")
            .destination(destination)
            .content(emailContent)
            .build()

        sesV2Client.sendEmail(sendEmailRequest)
    }
}