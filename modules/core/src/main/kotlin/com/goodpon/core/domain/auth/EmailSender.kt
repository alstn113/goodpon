package com.goodpon.core.domain.auth

interface EmailSender {

    fun sendVerificationEmail(name: String, email: String, verificationLink: String)
}