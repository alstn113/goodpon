package io.github.alstn113.payments.support.error

class CoreException(
    val errorType: ErrorType,
) : RuntimeException(errorType.message)