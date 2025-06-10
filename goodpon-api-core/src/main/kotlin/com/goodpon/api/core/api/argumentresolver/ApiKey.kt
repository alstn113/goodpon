package com.goodpon.api.core.api.argumentresolver

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiKey(
    val type: ApiKeyType,
)
