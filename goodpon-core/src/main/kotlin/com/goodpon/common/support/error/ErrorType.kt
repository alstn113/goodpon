package com.goodpon.common.support.error

enum class ErrorType(
    val statusCode: Int,
    val message: String,
    val errorLevel: ErrorLevel,
) {
    ACCOUNT_NOT_FOUND(404, "존재하지 않는 계정입니다.", ErrorLevel.WARN),
    ACCOUNT_PENDING(403, "이메일 인증이 완료되지 않았습니다.", ErrorLevel.WARN),

    MERCHANT_NOT_FOUND(404, "존재하지 않는 가맹점입니다.", ErrorLevel.WARN),

    INVALID_REQUEST(400, "잘못된 요청입니다.", ErrorLevel.WARN),
    UNAUTHORIZED(401, "인증되지 사용자입니다.", ErrorLevel.WARN),
    FORBIDDEN_REQUEST(403, "허용되지 않은 요청입니다.", ErrorLevel.WARN),

    INCORRECT_BASIC_AUTH_FORMAT(401, "잘못된 요청입니다. ':'를 포함해 인코딩해주세요.", ErrorLevel.WARN),
    COMMON_ERROR(500, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", ErrorLevel.ERROR),

}