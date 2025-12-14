package com.goodpon.dashboard.application.auth.service

fun interface VerificationTokenGenerator {

    fun generate(): String
}