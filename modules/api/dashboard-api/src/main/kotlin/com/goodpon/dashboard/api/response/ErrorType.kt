package com.goodpon.dashboard.api.response

import org.springframework.http.HttpStatus

enum class ErrorType(
    val status: HttpStatus,
    val message: String,
) {
    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

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

    // Merchant
    MERCHANT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상점입니다."),
    NO_MERCHANT_ACCESS_PERMISSION(HttpStatus.FORBIDDEN, "해당 상점에 접근할 수 있는 권한이 없습니다."),

    // Coupon Template
    COUPON_TEMPLATE_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "잘못된 쿠폰 템플릿 생성 요청입니다. 각 필드의 오류를 확인하세요."),
    COUPON_TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰 템플릿입니다."),
    COUPON_TEMPLATE_INVALID_STATUS_TO_PUBLISH(HttpStatus.BAD_REQUEST, "쿠폰 템플릿을 발행할 수 있는 상태가 아닙니다."),
    COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT(HttpStatus.FORBIDDEN, "상점이 소유한 쿠폰 템플릿이 아닙니다.")
    ;
}