package io.github.alstn113.payments.api.argumentresolver


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiKey(
    val type: ApiKeyType
)
