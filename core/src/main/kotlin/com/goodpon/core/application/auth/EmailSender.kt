package com.goodpon.core.application.auth

interface EmailSender {
    fun sendVerificationEmail(name: String, email: String, verificationLink: String)
}