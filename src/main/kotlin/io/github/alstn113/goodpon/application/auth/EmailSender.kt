package io.github.alstn113.goodpon.application.auth

interface EmailSender {
    fun sendVerificationEmail(name: String, email: String, verificationLink: String)
}