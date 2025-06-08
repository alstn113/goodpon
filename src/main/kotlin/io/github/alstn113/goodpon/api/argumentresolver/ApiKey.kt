package io.github.alstn113.goodpon.api.argumentresolver

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiKey(
    val type: ApiKeyType,
)
