package com.goodpon.api.partner.response

import org.springframework.http.HttpStatus

enum class ErrorType(
    val status: HttpStatus,
    val message: String,
) {
    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내 오류가 발생했습니다."),

    // idempotency
    INVALID_IDEMPOTENCY_KEY(HttpStatus.BAD_REQUEST, "멱등키는 300자 이하여야 합니다."),
    IDEMPOTENT_REQUEST_PROCESSING(HttpStatus.CONFLICT, "이전 먹등 요청이 아직 처리 중입니다. 잠시 후 다시 시도해주세요."),
    IDEMPOTENT_REQUEST_PAYLOAD_MISMATCH(HttpStatus.UNPROCESSABLE_ENTITY, "재시도된 요청의 본문이 처음 요청과 다릅니다. 멱등키를 새로 생성해주세요."),

    // security
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다. Client ID와 Client Secret을 확인해주세요."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    CLIENT_ID_MISSING(HttpStatus.BAD_REQUEST, "Client Id가 누락되었습니다."),
    CLIENT_SECRET_MISSING(HttpStatus.BAD_REQUEST, "Client Secret이 누락되었습니다."),

    // coupon - 400
    COUPON_ALREADY_ISSUED(HttpStatus.BAD_REQUEST, "사용자가 이미 발급한 쿠폰입니다."),
    COUPON_NOT_ISSUABLE_PERIOD(HttpStatus.BAD_REQUEST, "쿠폰을 발급할 수 있는 기간이 아닙니다."),
    COUPON_NOT_REDEEMABLE(HttpStatus.BAD_REQUEST, "해당 쿠폰을 사용할 수 있는 상태가 아닙니다."),
    COUPON_TEMPLATE_MAX_ISSUE_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "쿠폰 템플릿의 최대 발급 수량을 초과했습니다."),
    COUPON_TEMPLATE_MAX_REDEEM_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "쿠폰 템플릿의 최대 사용 수량을 초과했습니다."),
    COUPON_TEMPLATE_NOT_ISSUABLE(HttpStatus.BAD_REQUEST, "해당 쿠폰을 발급할 수 있는 상태가 아닙니다."),
    COUPON_TEMPLATE_REDEEM_CONDITION_NOT_MET(HttpStatus.BAD_REQUEST, "쿠폰 사용 조건을 충족하지 못했습니다."),
    USER_COUPON_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "해당 쿠폰은 이미 취소된 쿠폰입니다."),
    USER_COUPON_ALREADY_REDEEMED(HttpStatus.BAD_REQUEST, "해당 쿠폰은 이미 사용된 쿠폰입니다."),
    USER_COUPON_CANCEL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "해당 쿠폰은 사용 취소할 수 없는 상태입니다."),
    USER_COUPON_CANCEL_ORDER_ID_MISMATCH(HttpStatus.BAD_REQUEST, "쿠폰 사용 취소 시 쿠폰을 사용했던 주문 ID와 동일해야 합니다."),
    USER_COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "해당 쿠폰은 만료된 쿠폰입니다."),

    // coupon - 403
    COUPON_TEMPLATE_NOT_OWNED_BY_MERCHANT(HttpStatus.FORBIDDEN, "해당 쿠폰 템플릿은 현재 상점에서 소유하고 있지 않습니다."),
    USER_COUPON_NOT_OWNED_BY_USER(HttpStatus.FORBIDDEN, "해당 쿠폰은 사용자가 소유한 쿠폰이 아닙니다."),

    // coupon - 404
    COUPON_TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰 템플릿입니다."),
    USER_COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
}