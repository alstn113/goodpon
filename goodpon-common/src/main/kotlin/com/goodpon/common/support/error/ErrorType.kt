package com.goodpon.common.support.error

enum class ErrorType(
    val statusCode: Int,
    val message: String,
    val errorLevel: ErrorLevel,
) {
    INVALID_REQUEST(400, "잘못된 요청입니다.", ErrorLevel.WARN),
    BLANK_TOKEN(401, "토큰이 비어있습니다.", ErrorLevel.WARN),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", ErrorLevel.WARN),
    EXPIRED_TOKEN(401, "만료된 토큰입니다.", ErrorLevel.WARN),
    UNAUTHORIZED(401, "인증되지 않은 사용자입니다.", ErrorLevel.WARN),
    INCORRECT_BASIC_AUTH_FORMAT(401, "잘못된 요청입니다. ':'를 포함해 인코딩해주세요.", ErrorLevel.WARN),
    COMMON_ERROR(500, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", ErrorLevel.ERROR),
}