package io.github.alstn113.goodpon.support.error

class CoreException(
    val errorType: ErrorType,
) : RuntimeException(errorType.message)
