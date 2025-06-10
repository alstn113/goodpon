package com.goodpon.support.error

enum class ErrorType(
    val statusCode: Int,
    val message: String,
    val errorLevel: ErrorLevel,
) {

    INVALID_REQUEST(400, "잘못된 요청입니다.", ErrorLevel.WARN),
    COMMON_ERROR(500, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", ErrorLevel.ERROR),
}