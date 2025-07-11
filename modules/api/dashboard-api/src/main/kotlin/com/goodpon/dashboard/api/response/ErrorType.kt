package com.goodpon.dashboard.api.response

import org.springframework.http.HttpStatus

enum class ErrorType(
    val status: HttpStatus,
    val message: String,
) {
    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 오류가 발생했습니다."),

    // security
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    ACCOUNT_NOT_VERIFIED(HttpStatus.FORBIDDEN, "계정이 아직 인증되지 않았습니다."),

    // Account & Auth
    ACCOUNT_SIGN_UP_INVALID_INPUT(HttpStatus.BAD_REQUEST, "계정 생성에 필요한 입력값이 올바르지 않습니다."),
    ACCOUNT_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "이미 인증된 계정입니다."),
    ACCOUNT_EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    INVALID_EMAIL_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 인증 토큰입니다."),
}