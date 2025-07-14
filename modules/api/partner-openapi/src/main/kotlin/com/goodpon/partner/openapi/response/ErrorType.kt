package com.goodpon.partner.openapi.response

import org.springframework.http.HttpStatus

enum class ErrorType(
    val status: HttpStatus,
    val message: String,
) {
    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 오류가 발생했습니다."),

    // security
    CLIENT_ID_MISSING(HttpStatus.BAD_REQUEST, "Client Id가 누락되었습니다."),
    CLIENT_SECRET_MISSING(HttpStatus.BAD_REQUEST, "Client Secret이 누락되었습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다. Client ID와 Client Secret을 확인해주세요."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
}