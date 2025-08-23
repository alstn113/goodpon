package com.goodpon.application.dashboard.auth.service

fun interface VerificationTokenGenerator {

    fun generate(): String
}