package com.goodpon.core.support.error

enum class ErrorType(
    val statusCode: Int,
    val message: String,
    val errorLevel: ErrorLevel,
) {
    INVALID_REQUEST(400, "잘못된 요청입니다.", ErrorLevel.WARN),
    UNAUTHORIZED(401, "인증되지 사용자입니다.", ErrorLevel.WARN),
    FORBIDDEN_REQUEST(403, "허용되지 않은 요청입니다.", ErrorLevel.WARN),

    INVALID_ACCOUNT_EMAIL_FORMAT(400, "올바르지 않은 계정 이메일 형식입니다.", ErrorLevel.WARN),
    INVALID_ACCOUNT_PASSWORD_LENGTH(400, "계정 비밀번호는 8자 이상 100자 이하여야 합니다.", ErrorLevel.WARN),
    ACCOUNT_NAME_NOT_BLANK(400, "계정 이름은 공백으로만 이루어질 수 없습니다.", ErrorLevel.WARN),
    INVALID_ACCOUNT_NAME_LENGTH(400, "계정 이름은 50자 이하여야 합니다.", ErrorLevel.WARN),
    ACCOUNT_ALREADY_VERIFIED(400, "이미 인증된 계정입니다.", ErrorLevel.WARN),
    ACCOUNT_EMAIL_ALREADY_EXISTS(400, "이미 존재하는 계정 이메일입니다.", ErrorLevel.WARN),
    ACCOUNT_NOT_FOUND(404, "존재하지 않는 계정입니다.", ErrorLevel.WARN),
    ACCOUNT_PENDING(403, "이메일 인증이 완료되지 않았습니다.", ErrorLevel.WARN),

    INVALID_COUPON_POLICY_FIXED_AMOUNT_VALUE(400, "고정 금액 할인 값은 0보다 커야 합니다.", ErrorLevel.WARN),
    INVALID_COUPON_POLICY_FIXED_AMOUNT_MAX_AMOUNT(400, "고정 금액 할인은 최대 할인 금액을 설정할 수 없습니다.", ErrorLevel.WARN),
    INVALID_COUPON_POLICY_PERCENTAGE_VALUE(400, "백분율 할인 값은 1에서 100 사이여야 합니다.", ErrorLevel.WARN),
    INVALID_COUPON_POLICY_PERCENTAGE_MAX_AMOUNT(400, "백분율 할인의 경우 최대 할인 금액이 0보다 커야 합니다.", ErrorLevel.WARN),


    MERCHANT_NOT_FOUND(404, "존재하지 않는 가맹점입니다.", ErrorLevel.WARN),

    INCORRECT_BASIC_AUTH_FORMAT(401, "잘못된 요청입니다. ':'를 포함해 인코딩해주세요.", ErrorLevel.WARN),
    COMMON_ERROR(500, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", ErrorLevel.ERROR),
    ;
}