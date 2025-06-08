package io.github.alstn113.goodpon.application.email

interface EmailSender {

    fun sendEmail(to: String, subject: String, body: String)
}