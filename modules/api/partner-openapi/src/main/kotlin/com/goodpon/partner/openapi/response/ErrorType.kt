package com.goodpon.partner.openapi.response

import org.springframework.http.HttpStatus

enum class ErrorType(
    val status: HttpStatus,
    val message: String,
) {
    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 오류가 발생했습니다."),

    // coupon
    COUPON_TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰 템플릿입니다."),
    COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT(HttpStatus.FORBIDDEN, "해당 쿠폰 템플릿은 현재 상점에서 소유하고 있지 않습니다."),
    COUPON_ALREADY_ISSUED(HttpStatus.BAD_REQUEST, "사용자가 이미 발급한 쿠폰입니다."),
    COUPON_NOT_ISSUABLE_PERIOD(HttpStatus.BAD_REQUEST, "쿠폰을 발급할 수 있는 기간이 아닙니다."),
    COUPON_TEMPLATE_NOT_ISSUABLE(HttpStatus.BAD_REQUEST, "해당 쿠폰을 발급할 수 있는 상태가 아닙니다."),
    COUPON_TEMPLATE_MAX_ISSUE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "쿠폰 템플릿의 최대 발급 수량을 초과했습니다."),

    // security
    CLIENT_ID_MISSING(HttpStatus.BAD_REQUEST, "Client Id가 누락되었습니다."),
    CLIENT_SECRET_MISSING(HttpStatus.BAD_REQUEST, "Client Secret이 누락되었습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다. Client ID와 Client Secret을 확인해주세요."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
}